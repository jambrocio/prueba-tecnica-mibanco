package com.example.prueba_tecnica_mibanco.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="driver")
public class Driver {

	@Id
	private Long id;
	
	@NotNull
	private Integer dni;
	
	@NotEmpty
	private String nombres;
	
	@NotEmpty
	private String apellido_paterno;
	
	@NotEmpty
	private String apellido_materno;
	
	@NotEmpty
	private String email;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha_nacimiento;

	@Transient
	List<Vehicle> vehicles;
}
