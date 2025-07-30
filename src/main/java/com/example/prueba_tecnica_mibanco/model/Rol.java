package com.example.prueba_tecnica_mibanco.model;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="roles")
public class Rol {

	private Long id;
	private String name;
	
}
