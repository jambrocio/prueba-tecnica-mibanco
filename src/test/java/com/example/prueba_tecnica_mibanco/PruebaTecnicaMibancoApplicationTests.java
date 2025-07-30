package com.example.prueba_tecnica_mibanco;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.prueba_tecnica_mibanco.model.Driver;
import com.example.prueba_tecnica_mibanco.model.DriverDTO;
import com.example.prueba_tecnica_mibanco.model.PrimaRequest;
import com.example.prueba_tecnica_mibanco.model.UsageType;
import com.example.prueba_tecnica_mibanco.model.Vehicle;
import com.example.prueba_tecnica_mibanco.service.PersonService;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class PruebaTecnicaMibancoApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(PruebaTecnicaMibancoApplicationTests.class);
	
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
				log.info("UsageType values:");
				usageTypes.forEach(p -> {
					log.info("Name: ".concat(p.getName()));
				});
				
				Assertions.assertThat(usageTypes).isNotEmpty();
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
				log.info("Driver values:");
				drivers.forEach(p -> {
					log.info("Driver:".
						concat(String.valueOf(p.getDni())).concat(" => ").
						concat(p.getApellido_paterno()).concat(" ").
						concat(p.getApellido_materno()).concat(" ").
						concat(p.getNombres()));
				});
				Assertions.assertThat(drivers).isNotEmpty();
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
	void viewVehicleTest() {
		
		int vehicleId = 8;
		
		client.get()
			.uri("/vehicle" + "/{id}", Collections.singletonMap("id", String.valueOf(vehicleId)))
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(Vehicle.class)
			.consumeWith(response -> {
				Vehicle p = response.getResponseBody();
				Assertions.assertThat(p.getId() > 0).isTrue();
			});
			
	}
	
	@Test
	void viewDriverVehicleTest() {
		
		client.get()
			.uri("/driver" + "/7/vehicle/8")
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody(DriverDTO.class)
			.consumeWith(response -> {
				DriverDTO p = response.getResponseBody();
				Assertions.assertThat(p.getIdDriver() > 0).isTrue();
			});
			
	}
	
	@Test
	void viewCalculatePrimaTest() {
		PrimaRequest prima = new PrimaRequest();
		prima.setPrimaBase(500);
		prima.setDriverId(7L);
		prima.setVehicleId(8L);

		client.post()
			.uri("/calcular")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(prima), PrimaRequest.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.costo_seguro_vehiculo").value(value -> {
				Assertions.assertThat(Double.parseDouble(value.toString())).isGreaterThan(0);
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
				log.info("Token: ".concat(tokenValue.toString()));
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
	
	@Test
	void createVehicleTest() {
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
				log.info("Token: ".concat(tokenValue.toString()));
			})
			.returnResult().getResponseBody().toString();
		
		int minAnioVehicle = 1900; // El número más pequeño de 8 dígitos
        int maxAnioVehicle = 2025; // El número más grande de 8 dígitos

        Random randomAnioVehicle = new Random();
        int numeroAleatorioAnioVehicle = randomAnioVehicle.nextInt(maxAnioVehicle - minAnioVehicle + 1) + minAnioVehicle;
        
        Vehicle newVehicle = new Vehicle();
        newVehicle.setDriver_id((long) 1);
        newVehicle.setMarca("Toyota");
        newVehicle.setModelo("Rav 4");
        newVehicle.setUsage_type_id((long) 1);
        newVehicle.setYear(numeroAleatorioAnioVehicle);

		client.post()
			.uri("/vehicle")
			.header("Authorization", "Bearer " + token)
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(newVehicle), Vehicle.class)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.consumeWith(response -> {
				Assertions.assertThat(response.getStatus().is2xxSuccessful()).isTrue();
			});
	}
	
}