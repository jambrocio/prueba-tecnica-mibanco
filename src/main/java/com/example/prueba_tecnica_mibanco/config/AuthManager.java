package com.example.prueba_tecnica_mibanco.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;
/*
Se encarga de validar el token y recuperar las reclamaciones asociadas al mismo, como
usuario y roles. Estos datos son mapeados a un UsernamePasswordAuthenticationToken que es una
implementación de Authentication
*/
@Component
public class AuthManager implements ReactiveAuthenticationManager {
	
	@Value("${clave}")
	String CLAVE;
	
	//Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		//primeramente, transforma el Mono<Authentication> en un Mono<Claims>, incluyendo la información
    	//recibida en el token JWT. Después, ese Mono<Authentication> es transformado en un nuevo 
    	//Mono<Authentication> generado a partir de un UserPasswordAuthenticationToken que se configura
    	//a partir del usuario y roles del token
    	  return Mono.just(authentication)
    			  .map(auth->Jwts.parser()
    					 .verifyWith(Keys.hmacShaKeyFor(CLAVE.getBytes()))
    					 //.verifyWith(Jwts.SIG.HS256.key().build())
    					 .build()
    					 .parseSignedClaims(auth.getCredentials().toString().replace("Bearer ", ""))
    					 .getPayload())//Mono<Claims>
    			.switchIfEmpty(Mono.empty())
    			.map(claims->new UsernamePasswordAuthenticationToken(
    					claims.getSubject(),
                        null,
                        ((List<String>)claims.get("authorities", List.class)).stream()
                        	.map(SimpleGrantedAuthority::new)
                        	.collect(Collectors.toList()
                    )));//Mono<Authentication>
	}

}
