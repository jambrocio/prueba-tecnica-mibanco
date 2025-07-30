package com.example.prueba_tecnica_mibanco.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.prueba_tecnica_mibanco.model.Vehicle;

public interface VehicleRepository extends ReactiveCrudRepository<Vehicle, Long> {
	
}
