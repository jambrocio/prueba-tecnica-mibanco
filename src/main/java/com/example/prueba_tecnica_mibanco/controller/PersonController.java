package com.example.prueba_tecnica_mibanco.controller;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.example.prueba_tecnica_mibanco.model.Driver;
import com.example.prueba_tecnica_mibanco.model.PrimaRequest;
import com.example.prueba_tecnica_mibanco.model.UsageType;
import com.example.prueba_tecnica_mibanco.model.Vehicle;
import com.example.prueba_tecnica_mibanco.service.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PersonController {

	@Autowired
	private PersonService personService;
	
	@Tag(name = "listUsageTypes", description = "")
	@Operation(summary = "List Usage Types", description = "List Usage Types")
	@GetMapping(value="usageTypes")
	public ResponseEntity<Flux<UsageType>> listUsageType(){
		return new ResponseEntity<>(personService.listUsageType(),HttpStatus.OK);
	}
	
	@Tag(name = "findUsageType", description = "")
	@Operation(summary = "Find a usage type", description = "Find a usage type in the system")
	@GetMapping(value="usageType/{id}")
	public ResponseEntity<Mono<UsageType>> usageType(@PathVariable String id){
		return new ResponseEntity<>(personService.usageType(Long.valueOf(id)),HttpStatus.OK);
	}
	
	@Tag(name = "listDrivers", description = "")
	@Operation(summary = "List drivers", description = "List drivers")
	@GetMapping(value="drivers")
	public ResponseEntity<Flux<Driver>> drivers(){
		return new ResponseEntity<>(personService.listDrivers(),HttpStatus.OK);
	}
	
	@Tag(name = "findDriver", description = "")
	@Operation(summary = "Find a driver", description = "Find a driver in the system")
	@GetMapping(value="driver/{id}")
	public ResponseEntity<Mono<Driver>> driver(@PathVariable String id){
		return new ResponseEntity<>(personService.driver(Long.valueOf(id)),HttpStatus.OK);
	}
	
	@Tag(name = "createDriver", description = "")
	@Operation(summary = "Create a new driver", description = "Add a new driver to the system")
	@PostMapping(value="driver",consumes=MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Map<String, Object>>> altaDriver(@Valid @RequestBody Mono<Driver> monoDriver) {
		
		Map<String, Object> respuesta = new HashMap<String, Object>();
		
		return monoDriver.flatMap(driver -> {
			
			return personService.altaDriver(driver).map(p -> {
				respuesta.put("driver", p);
				respuesta.put("mensaje", "Driver creado con exito");
				respuesta.put("tmestamp", new Date());
				
				return ResponseEntity
					.created(URI.create("driver/".concat(String.valueOf(p.getId()))))
					.contentType(MediaType.APPLICATION_JSON)
					.body(respuesta);
			});
		}).onErrorResume(t -> {
			
			System.out.println(t);
			
			return Mono.just(t).cast(WebExchangeBindException.class)
					.flatMap(e -> Mono.just(e.getFieldErrors()))
					.flatMapMany(Flux::fromIterable)
					.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
					.collectList()
					.flatMap(list -> {
						respuesta.put("errors", list);
						respuesta.put("timestamp", new Date());
						respuesta.put("status", HttpStatus.BAD_REQUEST.value());
						return Mono.just(ResponseEntity.badRequest().body(respuesta));
					});
			
		});
				
	}
	
	@Tag(name = "findVehicle", description = "")
	@Operation(summary = "Find a vehicle", description = "Find a vehicle in the system")
	@GetMapping(value="vehicle/{id}")
	public ResponseEntity<Mono<Vehicle>> vehicle(@PathVariable String id){
		return new ResponseEntity<>(personService.vehicle(Long.valueOf(id)),HttpStatus.OK);
	}
	
	@Tag(name = "createVehicle", description = "")
	@Operation(summary = "Create a new vehicle", description = "Add a new vehicle to the system")
	@PostMapping(value="vehicle",consumes=MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Map<String, Object>>> altaVehicle(@Valid @RequestBody Mono<Vehicle> monoVehicle) {
		
		Map<String, Object> respuesta = new HashMap<>();
		
		return monoVehicle.flatMap(vehicle -> {
			
			Mono<UsageType> usageTypeMono = personService.usageType(vehicle.getUsage_type_id()).switchIfEmpty(Mono.error(new IllegalArgumentException("usage_type_id no válido")));
			Mono<Driver> driverMono = personService.driver(vehicle.getDriver_id()).switchIfEmpty(Mono.error(new IllegalArgumentException("driver_id no válido")));

			return Mono.zip(usageTypeMono, driverMono)
				.flatMap(tuple -> personService.altaVehicle(vehicle)
					.map(p -> {
						respuesta.put("vehicle", p);
						respuesta.put("mensaje", "Vehicle creado con exito");
						respuesta.put("tmestamp", new Date());
						return ResponseEntity
							.created(URI.create("vehicle/".concat(String.valueOf(p.getId()))))
							.contentType(MediaType.APPLICATION_JSON)
							.body(respuesta);
					})
				)
				.onErrorResume(e -> {
					respuesta.put("errors", e.getMessage());
					respuesta.put("timestamp", new Date());
					respuesta.put("status", HttpStatus.BAD_REQUEST.value());
					return Mono.just(ResponseEntity.badRequest().body(respuesta));
				});
		});

	}
	
	@GetMapping("/calcular")
    public Mono<ResponseEntity<Map<String, Object>>> calcularPrima(@RequestBody Mono<PrimaRequest> request) {
        Map<String, Object> respuesta = new HashMap<>();
        
        return request.flatMap(vehicle -> {
			
			Mono<Vehicle> vehicleMono = personService.vehicle(vehicle.getVehicleId()).switchIfEmpty(Mono.error(new IllegalArgumentException("vehicle_id no válido")));
			Mono<Driver> driverMono = personService.driver(vehicle.getDriverId()).switchIfEmpty(Mono.error(new IllegalArgumentException("driver_id no válido")));

			return Mono.zip(vehicleMono, driverMono)
				.flatMap(tuple -> personService.calcularPrima(vehicle)
					.map(p -> {
						respuesta.put("costo_seguro_vehiculo", p);
						respuesta.put("tmestamp", new Date());
						
						return ResponseEntity.badRequest().body(respuesta);
					})
				)
				.onErrorResume(e -> {
					respuesta.put("errors", e.getMessage());
					respuesta.put("timestamp", new Date());
					respuesta.put("status", HttpStatus.BAD_REQUEST.value());
					return Mono.just(ResponseEntity.badRequest().body(respuesta));
				});
		});
        
    }
}