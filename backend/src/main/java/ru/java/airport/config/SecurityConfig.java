package ru.java.airport.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.java.airport.constants.Constants;
import ru.java.airport.security.JwtTokenFilter;
import ru.java.airport.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(
                sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .exceptionHandling(exceptions ->
                exceptions
                    .authenticationEntryPoint(
                        (request, response, exception) -> {
                            response.setStatus(
                                HttpStatus.UNAUTHORIZED
                                    .value()
                            );
                            response.getWriter()
                                .write(Constants.UNAUTHORIZED);
                        })
                    .accessDeniedHandler(
                        (request, response, exception) -> {
                            response.setStatus(
                                HttpStatus.FORBIDDEN
                                    .value()
                            );
                            response.getWriter()
                                .write(Constants.UNAUTHORIZED);
                        }))
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .requestMatchers("/api/v1/flights/**").permitAll()  // просмотр рейсов
                    .requestMatchers("/api/v1/airlines/**").permitAll() // просмотр авиакомпаний
                    .requestMatchers("/api/v1/airports/**").permitAll() // просмотр аэропортов
                    .requestMatchers("/api/v1/aircrafts/**").permitAll() // просмотр самолетов
                    .requestMatchers("/api/v1/tickets/flight/**").permitAll() // просмотр билетов на рейс
                    .requestMatchers("/api/v1/tickets/**").permitAll() // просмотр свободных мест
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
