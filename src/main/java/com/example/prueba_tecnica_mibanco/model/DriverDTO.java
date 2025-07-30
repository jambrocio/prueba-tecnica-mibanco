package com.example.prueba_tecnica_mibanco.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {

	private Long idDriver;
	private Integer dni;
	private String nombres;
	private String apellido_paterno;
	private String apellido_materno;
	private String email;
	private LocalDate fecha_nacimiento;
	
	private int edad;
	private int anioVehiculo;
	private String marcaVehiculo;
	private String tipoUsoVehiculo;
}
