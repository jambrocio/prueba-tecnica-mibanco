package com.example.prueba_tecnica_mibanco.service;

import com.example.prueba_tecnica_mibanco.model.Driver;
import com.example.prueba_tecnica_mibanco.model.PrimaRequest;
import com.example.prueba_tecnica_mibanco.model.UsageType;
import com.example.prueba_tecnica_mibanco.model.Vehicle;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {
	
	Flux<UsageType> listUsageType();
	Mono<UsageType> usageType(Long usageTypeId);
	Mono<UsageType> usageTypeName(String name);
	
	Flux<Driver> listDrivers();
	Mono<Driver> driver(Long driverId);
	Mono<Driver> altaDriver(Driver driver);
	Mono<Driver> eliminarDriver(Long driverId);
	
	Mono<Vehicle> altaVehicle(Vehicle vehicle);
	Mono<Vehicle> vehicle(Long vehicleId);
	
	Mono<Driver> findDniDriver(int dni);
	
	Mono<Double> calcularPrima(PrimaRequest request);
	
}
