package com.project.webpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WebposApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebposApplication.class, args);
	}
}
