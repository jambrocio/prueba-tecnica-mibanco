package com.example.prueba_tecnica_mibanco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.prueba_tecnica_mibanco.model.Driver;
import com.example.prueba_tecnica_mibanco.model.DriverDTO;
import com.example.prueba_tecnica_mibanco.model.PrimaRequest;
import com.example.prueba_tecnica_mibanco.model.UsageType;
import com.example.prueba_tecnica_mibanco.model.Vehicle;
import com.example.prueba_tecnica_mibanco.repository.DriverRepository;
import com.example.prueba_tecnica_mibanco.repository.UsageTypeRepository;
import com.example.prueba_tecnica_mibanco.repository.VehicleRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonServiceImpl implements PersonService {
	
	@Autowired
	private UsageTypeRepository usageTypeRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Override
	public Flux<UsageType> listUsageType() {
		return usageTypeRepository.findAll();
	}

	@Override
	public Flux<Driver> listDrivers() {
		return driverRepository.findAll().flatMap(driver -> vehicleRepository.findByDriverId(driver.getId())
				.collectList()
				.map(vehicles -> {
					driver.setVehicles(vehicles);
					return driver;
				})
			);
	}
	
	@Override
	public Mono<Driver> driver(Long driverId) {
		return driverRepository.findById(driverId).flatMap(driver -> vehicleRepository.findByDriverId(driver.getId())
				.collectList()
				.map(vehicles -> {
					driver.setVehicles(vehicles);
					return driver;
				})
			);
	}
	
	@Override
	public Mono<DriverDTO> driverCustom(Long driverId, Long vehicleId) {
		
		return driverRepository.findById(driverId)
			.flatMap(driver -> vehicle(vehicleId)
				.flatMap(vehicle -> usageType(vehicle.getUsage_type_id())
					.map(usageType -> {
						DriverDTO dto = new DriverDTO();
						dto.setIdDriver(driver.getId());
						dto.setDni(driver.getDni());
						dto.setApellido_paterno(driver.getApellido_paterno());
						dto.setApellido_materno(driver.getApellido_materno());
						dto.setNombres(driver.getNombres());
						dto.setEmail(driver.getEmail());
						dto.setFecha_nacimiento(driver.getFecha_nacimiento());
						
						int edad = 0;
						if (driver.getFecha_nacimiento() != null) {
							java.time.LocalDate hoy = java.time.LocalDate.now();
							edad = java.time.Period.between(driver.getFecha_nacimiento(), hoy).getYears();
						}
						
						dto.setEdad(edad);
						dto.setAnioVehiculo(vehicle.getYear());
						dto.setMarcaVehiculo(vehicle.getMarca());
						dto.setTipoUsoVehiculo(usageType != null ? usageType.getName() : "Desconocido");
						
						return dto;
					})
				)
			);
		
	}

	@Override
	public Mono<Driver> altaDriver(Driver driver) {
		return driverRepository.save(driver);
	}

	@Override
	public Mono<Driver> eliminarDriver(Long driverId) {
		return null;
	}

	@Override
	public Mono<Vehicle> vehicle(Long vehicleId) {
		return vehicleRepository.findById(vehicleId);
	}

	@Override
	public Mono<Vehicle> altaVehicle(Vehicle vehicle) {
		return vehicleRepository.save(vehicle);
	}

	@Override
	public Mono<UsageType> usageType(Long usageTypeId) {
		return usageTypeRepository.findById(usageTypeId);
	}

	@Override
	public Mono<UsageType> usageTypeName(String name) {
		return usageTypeRepository.findByName(name);
	}

	@Override
	public Mono<Driver> findDniDriver(int dni) {
		return driverRepository.findByDni(dni);
	}

	@Override
	public Mono<Double> calcularPrima(PrimaRequest request) {
		Mono<Driver> driverMono = driverRepository.findById(request.getDriverId());
		Mono<Vehicle> vehicleMono = vehicleRepository.findById(request.getVehicleId())
			.doOnNext(vehicle -> {
				System.out.println("Vehicle values:");
				System.out.println("ID: " + vehicle.getId());
				System.out.println("Year: " + vehicle.getYear());
				System.out.println("Marca: " + vehicle.getMarca());
				System.out.println("Usage Type ID: " + vehicle.getUsage_type_id());
			});
		Mono<UsageType> usageTypeMono = usageTypeRepository.findByName("CARGA");

		return Mono.zip(driverMono, vehicleMono, usageTypeMono)
			.map(tuple -> {
				Driver driver = tuple.getT1();
				Vehicle vehicle = tuple.getT2();
				UsageType usageType = tuple.getT3();
				
				double porcentaje = 0.0;
				double primaBase = request.getPrimaBase();

				// Calcular edad según la fecha actual
				int edad = 0;
				if (driver.getFecha_nacimiento() != null) {
					java.time.LocalDate hoy = java.time.LocalDate.now();
					edad = java.time.Period.between(driver.getFecha_nacimiento(), hoy).getYears();
				}

				if (vehicle.getYear() > 2015) {
					porcentaje += 0.15;
				}
				
				if(usageType.getId() == vehicle.getUsage_type_id()) {
					porcentaje += 0.10;
				}
				
				if (edad > 50) {
					porcentaje -= 0.05;
				}

				String marca = vehicle.getMarca().toUpperCase();
				if ("BMW".equals(marca)) {
					porcentaje += 0.20;
				} else if ("AUDI".equals(marca)) {
					porcentaje += 0.10;
				}

				double primaFinal = primaBase * (1 + porcentaje);
				return Math.round(primaFinal * 100.0) / 100.0;
			});
	}

}