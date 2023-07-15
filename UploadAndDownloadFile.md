## Upload and Download File

Thông thường có 3 cách xử lý chính với File:
- Lưu File trong Database: Bảo mật hơn, tuy nhiên chỉ hợp với các file ít truy cập.
- Lưu File trong Directory/Folder: Kém bảo mật hơn nhưng lại tốt với các file thường xuyên cập.
- Lưu File trên Cloud (AMS S3, Google Cloud,...): Thường được sử dụng nhất, tận dụng được các ưu điểm của Cloud.

### Save File in Database
Đầu tiên tạo một entity chứa file 
```
package com.dev.studyspringboot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(length = 1000)
    private byte[] data;

}
```
Sau đó lần lượt tạo repository, service, controller tương tự như các phần trước.

Link tham khảo: https://www.youtube.com/watch?v=XUL60-Ke-L8

### Save File in Directory/Folder
Tương tự cách trên

Link tham khảo: https://www.youtube.com/watch?v=7L1BSy5pnGo

### Save File in AWS S3

Link tài liệu: https://docs.awspring.io/spring-cloud-aws/docs/3.0.1/reference/html/index.html#spring-cloud-aws-s3

Link video tham khảo: https://www.youtube.com/watch?v=vY7c7k8xmKE

Đầu tiên ta cần thêm vào poml
```
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>io.awspring.cloud</groupId>
			<artifactId>spring-cloud-aws-dependencies</artifactId>
			<version>3.0.1</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```
```
<dependency>
	<groupId>io.awspring.cloud</groupId>
	<artifactId>spring-cloud-aws-starter-s3</artifactId>
</dependency>
<!-- https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk-s3 -->
<dependency>
	<groupId>com.amazonaws</groupId>
	<artifactId>aws-java-sdk-s3</artifactId>
	<version>1.12.506</version>
</dependency>
```

Sau đó ta tạo một bucket trên AWS, và đọc tài liệu spring cloud để cấu hình
trong application.properties

Ví dụ:
```
# AWS S3 config
spring.cloud.aws.credentials.access-key=AKIAVIFENYAORGHAMKXJ
spring.cloud.aws.credentials.secret-key=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
spring.cloud.aws.region.static=ap-southeast-1
spring.cloud.aws.s3.endpoint=localhost:4566
```

Sau đó ta tiến hành thêm code vào `StorageService`