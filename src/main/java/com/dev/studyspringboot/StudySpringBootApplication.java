package com.dev.studyspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudySpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudySpringBootApplication.class, args);
	}

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello World";
	}
}

