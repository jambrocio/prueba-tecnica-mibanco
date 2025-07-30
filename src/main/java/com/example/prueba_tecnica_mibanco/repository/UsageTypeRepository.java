package com.example.prueba_tecnica_mibanco.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.prueba_tecnica_mibanco.model.UsageType;

import reactor.core.publisher.Mono;

public interface UsageTypeRepository extends ReactiveCrudRepository<UsageType, Long>{

	Mono<UsageType> findByName(String name);
}
