## Mail Service

Trong pần này mình sẽ giới thiệu cách send mail trong java. 

Đầu tiên ta thêm dependence vào poml
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

Tiếp theo ta tiến hành tạo một controller send mail đơn giản
```
package com.dev.studyspringboot.controller;

import com.dev.studyspringboot.dto.ApiResponse;
import com.dev.studyspringboot.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/mail")
@PreAuthorize("permitAll")
public class SendMailController {

    @Autowired
    private IMailService mailService;

    @PostMapping("")
    public ResponseEntity<?> sendMail(
            @RequestParam(value = "files")MultipartFile[] files,
            String to,
            String cc,
            String subject,
            String body
    ) {
        mailService.sendMail(files, to, cc, subject, body);

        ApiResponse response = ApiResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Send mail to + " + to + " successfully")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
```

Ta tạo mail service để send mail
```
package com.dev.studyspringboot.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class MailServiceImpl implements IMailService{

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(MultipartFile[] files, String to, String cc, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromMail);
            helper.setTo(to);
            if (!StringUtils.isEmpty(cc)) {
                helper.setCc(cc);
            }
            if (!StringUtils.isEmpty(subject)) {
                helper.setSubject(subject);
            } else {
                helper.setSubject("Mail System send from Ryo Website");
            }
            helper.setText(body);

            for (MultipartFile file : files) {
                helper.addAttachment(
                        Objects.requireNonNull(file.getOriginalFilename()),
                        new ByteArrayResource(file.getBytes())
                );
            }

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

Sau khi code xong ta cần config trên gmail để lấy password. Sau đó config
vào application.properties
```
#Mail config
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<your-email>
spring.mail.password=<your-password>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Cách hướng dẫn config ta có thể tra trên google.

Trên đây là cách send mail cơ bản. Tuy nhiên trong thực tế, ta cần nâng cấp thêm
để có thể tối ưu hóa nhất trải nghiệm người dùng.

Đầu tiên với nội dung: Trong thực tế mail được gửi nên có giao diện đẹp,
và tùy loại mail sẽ có các UI khác nhau. 

Thông thường chúng ta sẽ dùng html, css để làm UI cho mail. Sau đó lưu các
file mail này trên cloud, aws s3, ....

Khi tiến hành send mail ta sẽ download file html mail tương ứng về, sau đó
ta sẽ set các pram tương ứng vào file mail html nhờ freemarker.

Chúng ta sẽ tiến hành làm việc này:

Đầu tiên ta thêm thư viện freemarker vào poml:
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

Tiếp theo đó trong config ta cấu hình FreeMarkerConfig để chỉ ịnh vị trí 
lưu các email template. Ở ví dụ này mình sẽ lưu file template trong `resources/templates`.
```
package com.dev.studyspringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class FreeMarkerConfig {

    @Bean(name="freeMarkerConfigBean")
    public FreeMarkerConfigurationFactoryBean getFreeMarkerConfiguration(ResourceLoader resourceLoader) {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath("classpath:/templates/");
        return bean;
    }
}
```
 Bây giờ chúng ta quay lại MailServiceImpl để sử dụng template cho mail.
 ```
 package com.dev.studyspringboot.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class MailServiceImpl implements IMailService{

    @Value("${spring.mail.username}")
    private String fromMail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    @Qualifier("freeMarkerConfigBean")
    private Configuration emailConfiguration;

    @Override
    public void sendMail(MultipartFile[] files, String to, String cc, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromMail);
            helper.setTo(to);
            if (!StringUtils.isEmpty(cc)) {
                helper.setCc(cc);
            }
            if (!StringUtils.isEmpty(subject)) {
                helper.setSubject(subject);
            } else {
                helper.setSubject("Mail System send from Ryo Website");
            }
//            helper.setText(body);

            // using html template
            Template template = emailConfiguration.getTemplate("email1.html");
            Map<String, Object> params = new HashMap<>();
            params.put("name", "Anh Tài");
            params.put("location", "Hồ Chí Minh");
            params.put("phoneNumber", "0123456789");
            params.put("signature", "buianhtai.dev");
            params.put("content", body);

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
            helper.setText(html, true);

            for (MultipartFile file : files) {
                helper.addAttachment(
                        Objects.requireNonNull(file.getOriginalFilename()),
                        new ByteArrayResource(file.getBytes())
                );
            }

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
 ```

Các params name, location, phoneNumber, ... sẽ được chúng ta định nghĩa trong file
email1.html
```
...
<td valign="middle" class="hero bg_white"
    style="background: #ffffff; position: relative; z-index: 0;">
    <div style="padding: 1em 4em 0;">
        <h4
            style="font-family: 'Josefin Sans', sans-serif; color: #000000; margin-top: 0; font-weight: 400;">
            Dear ${name}, </h4>
        <h4
            style="font-family: 'Josefin Sans', sans-serif; color: #000000; margin-top: 0; font-weight: 400;">
            Lời đầu tiên chúng tôi rất vui vì bạn đã quan tâm đến trang web của chúng tôi.</h4>
        <h4
            style="font-family: 'Josefin Sans', sans-serif; color: #000000; margin-top: 0; font-weight: 400;">
            ${content}</h4>
        <h4
            style="font-family: 'Josefin Sans', sans-serif; color: #000000; margin-top: 0; font-weight: 400;">
            Chúng tôi hi vọng bạn sẽ có những trãi nghiệm tuyệt vời nhất. Dưới đây là thông tin của
            trang web chúng tôi nếu bạn quan tâm.</h4>
        <h4
            style="font-family: 'Josefin Sans', sans-serif; color: #000000; margin-top: 0; font-weight: 400;">
            Trân trọng,</h4>
        <h4
            style="font-family: 'Josefin Sans', sans-serif; color: #000000; margin-top: 0; font-weight: 400;">
            ${signature}</h4>
        <h4
            style="font-family: 'Josefin Sans', sans-serif; color: #000000; margin-top: 0; font-weight: 400;">
            ${location} - ${phoneNumber}</h4>
    </div>
</td>
...
```
Các biến sẽ được config như sau `${name}`, khi ta set params bên service thì giá trị
sẽ được truyền theo name như vậy.

Ngoài ra khi MimeMessageHelper getText là đoạn html của chúng ta thì sẽ tự động
bỏ qua các thẻ `style` vì vậy, nếu chúng ta cần css, chúng ta phải viết css inline.

Tất nhiên các bạn hoàn toàn có thể viết css trong style như bình thường sau đó
dùng tool để chuyển nó sang inline, bởi viết inline nó rất dài dòng và mất thời gian.

Tool mà mình sử dụng khi làm project này là Juice: https://github.com/Automattic/juice.


