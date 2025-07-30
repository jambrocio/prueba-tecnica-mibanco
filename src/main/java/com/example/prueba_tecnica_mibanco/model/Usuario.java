package com.example.prueba_tecnica_mibanco.model;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class Usuario {

	private Long id;
	private String username;
	private String password;
	private String nombres;
	private String apellido_paterno;
	private String apellido_materno;
	private String email;
	private boolean enabled;
		
}
