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
            params.put("phoneNumber", "0559920863");
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
