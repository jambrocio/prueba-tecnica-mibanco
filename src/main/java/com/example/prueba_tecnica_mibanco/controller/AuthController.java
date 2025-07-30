package com.example.prueba_tecnica_mibanco.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.prueba_tecnica_mibanco.model.Credentials;
import com.example.prueba_tecnica_mibanco.util.Constantes;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@RestController
public class AuthController {
	
	@Value("${clave}")
	String principalWord;
	
	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private static final long TIEMPO_VIDA=85_000_000;//aproximadamente 1 dia
	
	ReactiveUserDetailsService detailsService;

	public AuthController(ReactiveUserDetailsService detailsService) {
		this.detailsService = detailsService;
	}
	
	@Tag(name = "login", description = "")
	@Operation(summary = "Authentication", description = "Authentication in the system")
	@PostMapping(value="login", consumes = MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Credentials credentials){
		
		//si el usuario es válido genera un token con su información y se la envía al cliente
		//para que éste la utilice en las llamadas a los recursos
		return detailsService.findByUsername(credentials.getUser())
				.filter(details -> passwordEncoder.matches(credentials.getPwd(), details.getPassword()))
	            .map(details -> 
	            {
	            	Map<String, String> body = new HashMap<>();
	                body.put(Constantes.TOKEN, getToken(details));
	                body.put("username", credentials.getUser());
	                body.put(Constantes.MESSAGE, String.format("Hola %s has iniciado sesion con exito", credentials.getUser()));
	                
	            	return new ResponseEntity<>(body,HttpStatus.OK);
	            })
	            //.switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
	            .switchIfEmpty(Mono.fromSupplier(() -> {
	                Map<String, String> errorBody = new HashMap<>();
	                errorBody.put("message", "Error en la autenticación: username o password incorrecto");
	                return new ResponseEntity<>(errorBody, HttpStatus.UNAUTHORIZED);
	            }));
	            
	}
	
	//genera el token y lo envía al cliente
	private String getToken(UserDetails details) {
		//en el body del token se incluye el usuario 
		//y los roles a los que pertenece, además
		//de la fecha de caducidad y los datos de la firma
		return Jwts.builder()
				.subject(details.getUsername()) //usuario
				.issuedAt(new Date())
				.claim("authorities",details.getAuthorities().stream() //roles
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.expiration(new Date(System.currentTimeMillis() + TIEMPO_VIDA)) //fecha caducidad
				.signWith(Keys.hmacShaKeyFor(principalWord.getBytes()))//principalWord y algoritmo para firma
				//.signWith(Jwts.SIG.HS256.key().build())//clave y algoritmo para firma
				.compact(); //generación del token
		
	}
}
