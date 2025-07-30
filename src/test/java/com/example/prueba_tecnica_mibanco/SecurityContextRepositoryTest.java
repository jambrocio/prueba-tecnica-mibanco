package com.example.prueba_tecnica_mibanco;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;

import com.example.prueba_tecnica_mibanco.config.AuthManager;
import com.example.prueba_tecnica_mibanco.config.SecurityContextRepository;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class SecurityContextRepositoryTest {

    @Test
    void testSaveReturnsEmptyMono() {
        AuthManager authManager = Mockito.mock(AuthManager.class);
        SecurityContextRepository repository = new SecurityContextRepository(authManager);
        ServerWebExchange exchange = Mockito.mock(ServerWebExchange.class);
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mono<Void> result = repository.save(exchange, context);
        assertThat(result).isEqualTo(Mono.empty());
    }
}
