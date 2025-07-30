package com.example.prueba_tecnica_mibanco.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.prueba_tecnica_mibanco.model.Usuario;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<Usuario, Long> {

	@Query("select * from users where users.username = :username")
	Mono<Usuario> findByUsername(@Param("username") String username);
	
}
