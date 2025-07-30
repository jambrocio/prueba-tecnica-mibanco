package com.example.prueba_tecnica_mibanco.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.example.prueba_tecnica_mibanco.service.UserDetailService;


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration
public class SecurityConfig {

	private AuthManager authenticationManager;
	private SecurityContextRepository securityContextRepository;
	private UserDetailService userDetailService; 
	
    public SecurityConfig(AuthManager authenticationManager, SecurityContextRepository securityContextRepository,
			UserDetailService userDetailService) {
		this.authenticationManager = authenticationManager;
		this.securityContextRepository = securityContextRepository;
		this.userDetailService = userDetailService;
	}

	@Bean
    ReactiveUserDetailsService users() throws Exception{
		return user -> userDetailService.findByUsername(user);
	}

    @Bean
    SecurityWebFilterChain filter(ServerHttpSecurity http) throws Exception{
		//Configuracion de acceso a recursos
		return http.csrf(c -> c.disable())
		.authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)
		.authorizeExchange(auth -> auth
				//.pathMatchers(HttpMethod.POST, "/driver").hasAnyRole("ADMIN")
				//.pathMatchers(HttpMethod.POST, "/vehicle").hasAnyRole("ADMIN")
				.pathMatchers(HttpMethod.DELETE, "/eliminar/**").hasAnyRole("ADMIN", "OPERATOR")
				//.pathMatchers("/productos/**").authenticated()
				.anyExchange().permitAll()
		).build();
	}
}
