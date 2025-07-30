package com.example.prueba_tecnica_mibanco;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.prueba_tecnica_mibanco.model.Driver;
import com.example.prueba_tecnica_mibanco.model.UsageType;
import com.example.prueba_tecnica_mibanco.service.PersonService;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class PruebaTecnicaMibancoApplicationTests {

	@Autowired
	private WebTestClient client;
	
	@Autowired
	private PersonService service; 
	
	@Test
	void listUsageTypesTest() {
		client.get()
			.uri("/usageTypes")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(UsageType.class)
			//.hasSize(10)
			.consumeWith(response -> {
				List<UsageType> usageTypes = response.getResponseBody();
				usageTypes.forEach(p -> {
					System.out.println("nombre tipo uso : " + p.getName());
				});
				
				Assertions.assertThat(usageTypes.size() > 0).isTrue();
			});
	}
	
	@Test
	void viewUsageTypeTest() {
		
		String nameUsageType = "PERSONAL";
		UsageType usageType = service.usageTypeName(nameUsageType).block();
		
		client.get()
			.uri("/usageType" + "/{id}", Collections.singletonMap("id", usageType.getId()))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(UsageType.class)
			.consumeWith(response -> {
				UsageType p = response.getResponseBody();
				//Assertions.assertThat(p.getId().longValue() > 0).isTrue();
				Assertions.assertThat(p.getName()).isEqualTo(nameUsageType);
			});
			
	}
	
	@Test
	void listDriversTest() {
		client.get()
			.uri("/drivers")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Driver.class)
			//.hasSize(10)
			.consumeWith(response -> {
				List<Driver> drivers = response.getResponseBody();
				drivers.forEach(p -> {
					System.out.println("driver : " + p.getApellido_paterno() + " " + p.getApellido_materno() + " " + p.getNombres());
				});
				
				Assertions.assertThat(drivers.size() > 0).isTrue();
			});
	}
	
	@Test
	void viewDriverTest() {
		
		int dni = 42596272;
		Driver driver = service.findDniDriver(dni).block();
		
		client.get()
			.uri("/driver" + "/{id}", Collections.singletonMap("id", driver.getId()))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(Driver.class)
			.consumeWith(response -> {
				Driver p = response.getResponseBody();
				Assertions.assertThat(p.getDni() == dni).isTrue();
			});
			
	}
	
	@Test
	void createDriverTest() {
		String username = "FMALPARTIDA";
		String password = "123456";

		String token = client.post()
			.uri("/login")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue("{\"user\": \"" + username + "\", \"pwd\": \"" + password + "\"}")
			.exchange()
			.expectStatus().isOk()
			.expectBody()
			.jsonPath("$.token").value(tokenValue -> {
				Assertions.assertThat(tokenValue).isInstanceOf(String.class);
				System.out.println("TOKEN: " + tokenValue);
			})
			.returnResult().getResponseBody().toString();
		
		int min = 10000000; // El número más pequeño de 8 dígitos
        int max = 99999999; // El número más grande de 8 dígitos

        Random random = new Random();
        int numeroAleatorio = random.nextInt(max - min + 1) + min;
        
		Driver newDriver = new Driver();
		newDriver.setDni(numeroAleatorio);
		newDriver.setApellido_paterno("Perez");
		newDriver.setApellido_materno("Gomez");
		newDriver.setNombres("Juan");
		newDriver.setEmail("juan.perez@gmail.com");
		newDriver.setFecha_nacimiento(java.time.LocalDate.parse("1980-01-08"));

		client.post()
			.uri("/driver")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(newDriver), Driver.class)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.consumeWith(response -> {
				Assertions.assertThat(response.getStatus().is2xxSuccessful()).isTrue();
			});
	}
	
	
}