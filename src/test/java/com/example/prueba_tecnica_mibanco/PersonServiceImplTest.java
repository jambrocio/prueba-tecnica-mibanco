package com.example.prueba_tecnica_mibanco;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

import com.example.prueba_tecnica_mibanco.model.Driver;
import com.example.prueba_tecnica_mibanco.model.PrimaRequest;
import com.example.prueba_tecnica_mibanco.model.UsageType;
import com.example.prueba_tecnica_mibanco.model.Vehicle;
import com.example.prueba_tecnica_mibanco.service.PersonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = "50000")
class PersonServiceImplTest {

	@Mock
	private PersonService personService;

	@Test
	void testListDrivers() {
		Flux<Driver> drivers = Flux.just(new Driver());
		Mockito.when(personService.listDrivers()).thenReturn(drivers);
		assertThat(personService.listDrivers().collectList().block()).isNotEmpty();
	}

	@Test
	void testDriver() {
		Driver driver = new Driver();
		driver.setId(1L);
		Mockito.when(personService.driver(1L)).thenReturn(Mono.just(driver));
		assertThat(personService.driver(1L).block()).isEqualTo(driver);
	}

	@Test
	void testVehicle() {
		Vehicle vehicle = new Vehicle();
		vehicle.setId(1L);
		Mockito.when(personService.vehicle(1L)).thenReturn(Mono.just(vehicle));
		assertThat(personService.vehicle(1L).block()).isEqualTo(vehicle);
	}

	@Test
	void testUsageType() {
		UsageType usageType = new UsageType();
		usageType.setId(1L);
		Mockito.when(personService.usageType(1L)).thenReturn(Mono.just(usageType));
		assertThat(personService.usageType(1L).block()).isEqualTo(usageType);
	}

	@Test
	void testCalcularPrima() {
		PrimaRequest request = new PrimaRequest();
		Mockito.when(personService.calcularPrima(any(PrimaRequest.class))).thenReturn(Mono.just(100.0));
		assertThat(personService.calcularPrima(request).block()).isEqualTo(100.0);
	}
}