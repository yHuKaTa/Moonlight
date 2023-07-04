package com.aacdemy.moonlight.config.security;

import com.aacdemy.moonlight.entity.user.User;
import com.aacdemy.moonlight.entity.user.UserRole;
import com.aacdemy.moonlight.exception.AccessDeniedHandlerMoonImpl;
import com.aacdemy.moonlight.exception.AppExceptionHandler;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final Filter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    private AccessDeniedHandlerMoonImpl customAccessDeniedHandler;

    private final AppExceptionHandler appExceptionHandler;

    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    @Bean //must be
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint) //our delegated authentication entry point
                .and()
                .csrf().disable()
                .authorizeHttpRequests()
                //.requestMatchers("/users/**").authenticated()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("files/download/**").permitAll()
                .requestMatchers("/users/login").permitAll()
                .requestMatchers("/transfers/addTransfer").permitAll()
                .requestMatchers("/transfers").permitAll()
                .requestMatchers("/transfers/**").hasRole("ADMIN")
                .requestMatchers("/files/images/add").hasRole("ADMIN")
                .requestMatchers("/files/room").hasRole("ADMIN")
                .requestMatchers("/roomReservations/delete/**").hasRole("ADMIN")
                .requestMatchers("/cars/addCar").hasRole("ADMIN")
                .requestMatchers("/carCategory/addCategory").hasRole("ADMIN")
                .requestMatchers("/screen-event/**").permitAll()
                //.requestMatchers("/cars/**").permitAll()
                //.anyRequest().authenticated()
                .anyRequest().permitAll()  //for this moment
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //filter before token
        return http.build();
    }
}

