package com.example.prueba_tecnica_mibanco.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.prueba_tecnica_mibanco.model.Rol;

import reactor.core.publisher.Flux;

public interface RolRepository extends ReactiveCrudRepository<Rol, Long> {

	@Query("SELECT ro.id, ro.name FROM roles ro JOIN users_roles uo ON ro.id = uo.role_id WHERE uo.user_id = :userId")
	Flux<Rol> findByIdUser(@Param("userId") Long userId);
	
}
