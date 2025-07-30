package com.example.prueba_tecnica_mibanco.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.prueba_tecnica_mibanco.model.Driver;

import reactor.core.publisher.Mono;

public interface DriverRepository extends ReactiveCrudRepository<Driver, Long>{

	Mono<Driver> findByDni(int dni);
	
}
