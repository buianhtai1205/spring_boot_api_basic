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


### Save File in Directory/Folder

### Save File in AWS S3