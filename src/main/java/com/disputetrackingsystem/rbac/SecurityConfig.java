package com.disputetrackingsystem.rbac;

import com.disputetrackingsystem.rbac.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    //AUTHENTICATION
    //Bean to change the AuthenticationProvider itself or customize it
    // and for this to work we need a DB from which data can be fetched and authenticated
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();   //to fetch data from DB

        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());       //to store plain text pwd in DB (for dev use only)
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));     //to store hash my pwd & store in DB

        //to use this method we have to create a custom class that implements customUserDetailsService
        provider.setUserDetailsService(customUserDetailsService);               //fetches the User entity from DB
        //by default UserDetailsService checks the user&pass, so I also want it to check & authenticate the details from DB

        return provider;
    }


    //AUTHORIZATION
    //Bean to change the default security filter chain provided by spring security
    // and for this to work we need to create a custom class that implements UserDetailsService
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(customizer -> customizer.disable())   //disable csrf token security
                .cors(Customizer.withDefaults())   // 👈 automatically picks up your CorsConfigurationSource bean                     //enable CORS for all origins (for dev only, for prod specify the origins)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user").permitAll()            //tells spring no authentication required for any API with endpoint /user
                        .anyRequest().authenticated())        //tells spring to authenticate every request going through this security chain filter.
                .httpBasic(Customizer.withDefaults())        //to see the actual response in th postman for REST API access.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))    //tells spring security to keep my http stateless.
                .build();       //its job is to return the object of security filter chian
    }
}
