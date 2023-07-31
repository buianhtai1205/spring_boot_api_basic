## Stage 3: Handle Exception and Validation
### Exception basic
Trong package exception định nghĩa các exception cần thiết
```
package com.dev.studyspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NullException extends RuntimeException{
    public NullException(String message) {
        super(message);
    }
}
```
...

Tạo một GlobalExceptionHandler xử lý các exception trên
```
package com.dev.studyspringboot.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullException.class)
    public ResponseEntity<String> handleNullException(NullException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(WarningException.class)
    public ResponseEntity<String> handleWarningException(WarningException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
```
### Consistent response format
Ta đã cấu trúc sơ bộ exception, nó đã gần như ổn và tất nhiên là đã có thể sữ dụng
được, tuy nhiên đó là về phía backend.

Ta sẽ nghĩ cho bên phía frontend nữa. Các data, message, và exception ta đang trả về
nó đang bị hơi loạn.
- Có lúc là các data từ service trả về.
- Có lúc là các message truyền vào từ exception và được trả về từ GlobalExceptionHandler
- Có lúc lại là các exception của System, Framework, nó trả về một lượng lớn message không
  cần thiết quá bên phía frontend.

Chính vì thế mình sẽ làm cho tất cả chúng được đồng bộ (consistent) thành format:
```
{
  "statusCode": 200|201|304|... ,
  "message": "Get product success"|"..." ,
  "data": {} | [{}] ...
}
```
Đầu tiên ta tạo `DefaultResponse` trong dto:
```
package com.dev.studyspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultResponse {
    private int statusCode;
    private String message;
    private Object data;
}
```
Giờ ta tiến hành custom lại GlobalExceptionHandler của chúng ta
```
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullException.class)
    public ResponseEntity<DefaultResponse> handleNullException(NullException ex) {
        DefaultResponse apiResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DefaultResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        DefaultResponse apiResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<DefaultResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        DefaultResponse apiResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @ExceptionHandler(WarningException.class)
    public ResponseEntity<DefaultResponse> handleWarningException(WarningException ex) {
        DefaultResponse apiResponse = DefaultResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
```
Oke, theo đó ta sẽ tiến hành sửa lại các response trong Các `Controller` và `Service` nếu có
để chuẫn format đồng bộ.

Trong quá trình dò các file và sửa mình có nhận thấy một exception mình throw lúc kiểm tra
authentication `BadCredentialsException`.

Đây như mình nói là một exception của hệ thống. Mình sẽ tiến hành config nó trong
`GlobalExceptionHandler` để nó cũng trả về dạng mình mong muốn
```
// Handle BadCredentialsException return DefaultResponse
@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<DefaultResponse> handleBadCredentialsException(BadCredentialsException ex) {
    DefaultResponse response = DefaultResponse.builder()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .message(ex.getMessage())
            .build();
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
}
```

Ngoài ra với các exception không mong muốn khác mà mình chưa định nghĩa hoặc chưa phát hiện
mình cũng sẽ config nốt ở cuối file GlobalExceptionHandler để nó return dạng mình muốn.
```
/ Handle others exception not define
@ExceptionHandler(Exception.class)
public ResponseEntity<DefaultResponse> handleException(Exception ex) {
    DefaultResponse response = DefaultResponse.builder()
            .statusCode(500)
            .message(ex.getMessage())
            .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
}
```
Đến đây thì mọi thứ chắc đã oke. Nếu sau này có exception khác thì mình sẽ config để trên
exception này là mọi thứ hoạt động tốt.

### Validation
Trong phần này, ta sẽ tìm hiểu cách validate ở phía backend trong spring boot.

Ta đã handle các exception một cách cẫn thận trong service nên trong phần này
ta sẽ validate các trường mà ta cho là không thể để trống.

Bản thân mình sẽ ưu tiên và validate các field, các feature mà ở hướng user
nhiều hơn là bên admin.

Ví dụ trong project này, mình sẽ validate tại `AuthRequest`(người dùng đăng nhập),
`Order`(người dùng đặt hàng), `User`(người dùng đăng ký).

Đầu tiên để sử dụng validation ta cần thêm vào pom
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
Tiếp theo ta vào `dto/AuthRequest` thêm Annotation cho hai trường ta muốn validate
```
public class AuthRequest {
    @NotEmpty(message = "Username is EMPTY!")
    private String username;
    @NotEmpty(message = "Password is EMPTY!")
    private String password;
}
```
Ta truyền `message` ở đây để có thể get nó và trả về phía client nếu hai trường đó trống.

Tiếp đến ta vào Controller mà ta muốn validate thêm từ annotation `@Validated` vào `AuthRequest`
```
@PostMapping("/authenticate")
public ResponseEntity<?> authenticateAndGetJwt(@Validated @RequestBody AuthRequest authRequest )
{
    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(
            authRequest.getUsername(),
            authRequest.getPassword()
    ));
    if (authentication.isAuthenticated()) {
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequest.getUsername());
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(jwtService.generateToken(authRequest.getUsername()))
                .token(refreshToken.getToken())
                .build();
        return ResponseEntity.ok(jwtResponse);
    } else throw new RuntimeException("Internal Server Error");
}
```
Khi ta thêm vaidate, nếu trong trường hợp AuthRequest không vượt qua được validate thì code sẽ không run
bên trong hàm trên, Và nó sẽ trả ra một `BindException`.

Phần exception mình đã trình bày ở trên, công việc của ta ngay lúc này đơn giản là vào `GlobalExceptionHandler`
và config lại BindException.
```
// config exception validate
@ExceptionHandler(BindException.class)
public ResponseEntity<DefaultResponse> handleBindException(BindException ex) {
    DefaultResponse response = DefaultResponse.builder()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .message(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
}
```
Mình đã thêm phần code này vào `GlobalExceptionHandler`, ờ đây mình trả về message là message mà ta
đã config trong `AuthRequest` ở trên, và mình sẽ trả về message đầu tiên.

Giả sử cả username vả password đều empty thì sẽ trả về message đầu tiên check được là `Username is EMPTY!`

Tương tự mình làm với `OrderDTO` và `UserDTO`.