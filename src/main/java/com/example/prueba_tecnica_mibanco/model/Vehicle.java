package com.example.prueba_tecnica_mibanco.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="vehicle")
public class Vehicle {

	@Id
	private Long id;
	
	@NotNull
	private int year;
	
	@NotEmpty
	private String marca;
	
	@NotEmpty
	private String  modelo;
	
	@NotNull
	private Long usage_type_id;
	
	@NotNull
	private Long driver_id;
		  
}
