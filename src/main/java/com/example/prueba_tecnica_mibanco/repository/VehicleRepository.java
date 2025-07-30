package com.example.prueba_tecnica_mibanco.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.prueba_tecnica_mibanco.model.Vehicle;

import reactor.core.publisher.Flux;

public interface VehicleRepository extends ReactiveCrudRepository<Vehicle, Long> {
	
	@Query("select * from vehicle where vehicle.driver_id = :driverId")
	Flux<Vehicle> findByDriverId(Long driverId);
}
