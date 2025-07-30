package com.example.prueba_tecnica_mibanco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.prueba_tecnica_mibanco.model.Usuario;
import com.example.prueba_tecnica_mibanco.repository.RolRepository;
import com.example.prueba_tecnica_mibanco.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class UserDetailServiceImpl implements UserDetailService {

	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByUsername(username) //Mono<Usuario>
                	.flatMap((Usuario us) -> rolRepository.findByIdUser(us.getId())   //Flux<Rol>      		
                								.map(r -> r.getName()) //Flux<String>
                        .collectList()//Mono<List<String>>
                        .map(roles -> User.withUsername(us.getUsername())
                                .password(us.getPassword())
                                .roles(roles.toArray(new String[0]))
                                .build())) //Mono<UserDetails>
                .switchIfEmpty(Mono.empty());
	}

}
