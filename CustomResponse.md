## Custom Response

Trong các phần trước khi sử dụng Jpa ta đang query và trả về
gần như là full các field, điều đó đang vi phạm về an toàn thông
tin + bên fe sẽ thấy một lượng loớn data không cần thiết, vì vậy
ta nên custom lại. 

Ví dụ với api get list brand ta đã làm:
```
@GetMapping("/")
public ResponseEntity<?> getAllBrand()
{
    List<Brand> brands = iBrandService.getAllBrand();
    ApiResponse response = ApiResponse.builder()
            .statusCode(HttpStatus.OK.value())
            .message("Get list brand successfully")
            .data(brands)
            .build();
    return new ResponseEntity<>(response, HttpStatus.OK);
}
```

Để sửa lại response ta sẽ tạo một package mới là response và
tạo file BrandResponse để chỉ định các field cần thiết
```
package com.dev.studyspringboot.response.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {

    private String name;

    private String address;

    private String imageUrl;
}
```

Sau khi tạo xong, ta có thể có hai option để custom

Option1: Ta sửa thẳng trong controller
```
    @GetMapping("/option1") // worst option
    public ResponseEntity<?> getBrands() {
        List<Brand> brands = iBrandService.getAllBrand();

        List<BrandResponse> brandResponseList = brands.stream()
                .map(brand -> {
                    return BrandResponse.builder()
                            .name(brand.getName())
                            .address(brand.getAddress())
                            .imageUrl(brand.getImageUrl())
                            .build();
                })
                .collect(Collectors.toList());

        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list brand successfully")
                .data(brandResponseList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
```

Như mình comment, mặc dù nó thật sự solve problem nhưng việc 
làm controller trở nên phình ra thì thật sự không clean code.

Tuy nhiên, nếu source code của chúng ta đã quá lớn và quá khó
để có thể sửa toàn bộ source thì option này có thể thực thi
để cứu cánh.

Option2: Ta sửa service thay vì trả về List<Brand> ta sẽ cho nó
trả về List<BrandResponse> và gọi như bình thường.
```
    @GetMapping("/option2") // best option
    public ResponseEntity<?> getBrands2() {
        List<BrandResponse> brandResponseList = iBrandService.getBrands();
        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get list brand successfully")
                .data(brandResponseList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
```

Với cách 2, ta có thể đưa đoạn coverter trong cách 1 vào service
khi đó code bên controller sẽ clean hơn.

Mở rộng:

Đến đây mọi thứ đã có vẻ ổn, chỉ có một vấn đề ở đoạn converter
nó sẽ được lặp đi lặp lại nếu ta có nhiều api get, ví dụ
- get list brands
- get brand
- get list brands by deleted at !=/== null
- get list brands by name like ....
- ....

Lúc đó đoạn converter sẽ bị lặp và làm code ta trở nên không còn clean.

Lúc này ta sẽ nghĩ đến đưa đoạn coverter ra thành hàm riêng để gọi.

Có một thư viện có thể giúp ta làm điều đó.

Đầu tiên ta thêm dependence vào poml
```
<!-- MapStruct dependencies -->
<dependency>
	<groupId>org.mapstruct</groupId>
	<artifactId>mapstruct</artifactId>
	<version>1.4.2.Final</version>
</dependency>
<dependency>
	<groupId>org.mapstruct</groupId>
	<artifactId>mapstruct-processor</artifactId>
	<version>1.4.2.Final</version>
	<scope>provided</scope>
</dependency>
```

Tiếp đến ta tiến hành tạo thêm một package converter lưu các Mapper

Sau đó ta tạo BrandMapper và config như sau:
```
package com.dev.studyspringboot.converter;

import com.dev.studyspringboot.model.Brand;
import com.dev.studyspringboot.response.brand.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    @Mappings({
            @Mapping(target = "name", source = "name"),
//            @Mapping(target = "name", source = "name", qualifiedByName = "getNameCustom"),
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "imageUrl", source = "imageUrl")
    })
    BrandResponse brandToBrandResponse(Brand brand);

    @Named("getNameCustom")
    default String getNameCustom(String name) {
        return "Custom: " + name;
    }
}

```

Với các tên trùng của hai Brand và BrandRespone, ta hoàn toàn không 
cần @Mapping (ở đây mình chỉ viết vào để rõ code). Chúng ta chỉ cần 
config các field khác name. 

Ngoài ra ta có thể custom lại data như field name trên kia,
ta sử dụng qualifiedByName và define bên dưới để thay đổi data get từ 
database. 

Oke xong phần config, để sữ dụng nó ta sử dụng như sử sụng repository

Ta khai báo trong BrandServiceImpl
```
@Autowired
private BrandMapper brandMapper;
```

Sau đó dùng nó như bình thường:
```
    @Override
    public List<BrandResponse> getBrands() {
        List<Brand> brands = brandRepository.findAllByDeletedAtIsNull();
        return brands.stream()
                .map(brand -> brandMapper.brandToBrandResponse(brand))
                .collect(Collectors.toList());
    }
```