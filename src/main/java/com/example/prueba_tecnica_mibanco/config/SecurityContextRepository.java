package com.example.prueba_tecnica_mibanco.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
/*
Utilizada por Spring Security, carga el contexto de seguridad durante el intercambio de solicitudes.
Utilizando el authenticationmanager, extrae el token
de la solicitud, autentica y crea un contexto de seguridad basado en la información del token
*/
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
	private AuthManager authenticationManager;
	
	public SecurityContextRepository(AuthManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		return Mono.empty();
	}
	 //obtiene el token y le pasa la información al AuthManager para que lo valide y extraiga los datos
    //el resultado es mapeado al contexto de seguridad de Spring para realizar la autenticación y autorización
	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith("Bearer ")) //Mono<String>
                .flatMap(authHeader -> 
                		this.authenticationManager.authenticate(
                			new UsernamePasswordAuthenticationToken(authHeader.substring(7), authHeader.substring(7))
                		)
                    	.map(SecurityContextImpl::new)
                );
	}

}
