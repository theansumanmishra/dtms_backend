package com.disputetrackingsystem.security.config;

import com.disputetrackingsystem.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //AUTHENTICATION
    //Bean to change the AuthenticationProvider itself or customize it
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();   //to fetch data from DB

        provider.setUserDetailsService(customUserDetailsService);               //fetches the User entity from DB
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));     //to store hash my pwd & store in DB

        return provider;
    }

    //AUTHORIZATION
    //Bean to change the default security filter chain provided by spring security
    // and for this to work we need to create a custom class that implements UserDetailsService
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(customizer -> customizer.disable())   //disable csrf token for Stateless HTTP
                .cors(Customizer.withDefaults())                                     //automatically picks up your CorsConfigurationSource bean                     //enable CORS for all origins (for dev only, for prod specify the origins)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login",
                                "/forget-password",
                                "/reset-link",
                                "/reset-password")
                        .permitAll()                                                //tells spring no authentication required for any API with endpoint /user
                        .anyRequest().authenticated())                              //tells spring to authenticate every request going through this security chain filter.
                .httpBasic(Customizer.withDefaults())                               //to see the actual response in th postman for REST API access.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))    //tells spring security to keep my http stateless.
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();                                                           //its job is to return the object of security filter chain
    }
}
