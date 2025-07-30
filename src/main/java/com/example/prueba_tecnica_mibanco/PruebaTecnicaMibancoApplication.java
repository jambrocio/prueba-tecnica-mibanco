package com.example.prueba_tecnica_mibanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	info = @Info(
			title = "Mibanco API",
			version = "1.0",
			description = "API documentation for managing drivers and usage types in Mibanco system"
	)
)
@SpringBootApplication
public class PruebaTecnicaMibancoApplication {

	public static void main(String[] args) {
		SpringApplication.run(PruebaTecnicaMibancoApplication.class, args);
	}

}
