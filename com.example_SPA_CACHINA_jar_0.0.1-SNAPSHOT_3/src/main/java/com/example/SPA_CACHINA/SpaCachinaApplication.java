package com.example.SPA_CACHINA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.SPA_CACHINA")
public class SpaCachinaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaCachinaApplication.class, args);
	}

}
