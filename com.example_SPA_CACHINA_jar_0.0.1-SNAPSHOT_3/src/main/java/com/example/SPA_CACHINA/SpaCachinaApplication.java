package com.example.SPA_CACHINA;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.SPA_CACHINA")
public class SpaCachinaApplication {

	@PostConstruct
	public void init() { // Zona horaria por defecto: Perú/Lima
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

	public static void main(String[] args) { // Punto de entrada de Spring Boot
		SpringApplication.run(SpaCachinaApplication.class, args);
	}

}
