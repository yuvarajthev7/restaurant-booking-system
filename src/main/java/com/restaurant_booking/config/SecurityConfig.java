package com.restaurant_booking.config;

import com.restaurant_booking.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // <--- MAKE SURE TO IMPORT THIS
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public UserDetailsService userDetailsService(UserRepository userRepo) {
    //     return email -> userRepo.findByEmail(email)
    //             .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    // }

    // @Bean
    // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http
    //         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    //         .csrf(csrf -> csrf.disable())
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/api/users/**").permitAll()
    //             .requestMatchers("/api/restaurants/**").permitAll()
    //
    //             // --- FIXED RULES ---
    //             // Allow ANYONE to CREATE a reservation (POST)
    //             .requestMatchers(HttpMethod.POST, "/api/reservations").permitAll()
    //
    //             // Only ADMINs can VIEW the list (GET)
    //             .requestMatchers(HttpMethod.GET, "/api/reservations").hasRole("ADMIN")
    //
    //             .anyRequest().authenticated()
    //         )
    //         .httpBasic(Customizer.withDefaults());
    //
    //     return http.build();
    // }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        // CHANGED: Look up by Government ID
        return govId -> userRepo.findByGovernmentId(govId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Gov ID: " + govId));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/**").permitAll() // Sign up
                .requestMatchers("/api/restaurants/**").permitAll()

                // Allow POST reservation (User logged in)
                .requestMatchers(HttpMethod.POST, "/api/reservations").authenticated()

                // Admin ONLY: GET (view) and DELETE (remove)
                .requestMatchers(HttpMethod.GET, "/api/reservations").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
