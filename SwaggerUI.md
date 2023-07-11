### OpenAI - Swagger UI

- Tài liệu tham khảo của spring : https://springdoc.org/
- Link video tham khảo: https://www.youtube.com/watch?v=2o_3hjUPAfQ
- Hướng dẫn cấu hình cơ bản:

Add dependence:
```
<dependency>
	<groupId>org.springdoc</groupId>
	<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
	<version>2.1.0</version>
</dependency>
```

Tiếp theo tạo một class `OpenApiConfig` trong config:
```
package com.dev.studyspringboot.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Ryo",
                        email = "buianhtai2017tq@gmail.com",
                        url = "https://github.com/buianhtai1205"
                ),
                description = "OpenApi documentation for Spring Boot",
                title = "OpenApi specification - Ryo",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://example.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8082"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "https://buianhtai.dev"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
```