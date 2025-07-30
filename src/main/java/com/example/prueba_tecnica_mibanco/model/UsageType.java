package com.example.prueba_tecnica_mibanco.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usage_type")
public class UsageType {
	
	@Id
	private Long id;
	private String name;
	
}
