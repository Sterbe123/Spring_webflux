package cl.sterbe.app.configurations.security;

import cl.sterbe.app.componets.security.AuthenticationManager;
import cl.sterbe.app.componets.security.FilterManager;
import cl.sterbe.app.componets.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ServerSecurityContext serverSecurityContext;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, FilterManager filterManager){

        return http.cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                })).and()
                .csrf().disable()
                .addFilterAfter(filterManager, SecurityWebFiltersOrder.FIRST)
                .authenticationManager(authenticationManager)
                .securityContextRepository(serverSecurityContext)
                .authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .anyExchange()
                .authenticated()
                .and().build();

    }
}
