//package com.backenddiploma.config;
//
//import com.backenddiploma.security.TokenFilter;
//import com.backenddiploma.services.security.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.HttpStatusEntryPoint;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final UserService userService;
//    private final TokenFilter tokenFilter;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public AuthenticationManagerBuilder configureAuthenticationManagerBuilder(
//            AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
//        return authenticationManagerBuilder;
//    }
//
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(
//                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/secured/user").fullyAuthenticated()
//                        .anyRequest().permitAll())
//                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
//        return httpSecurity.build();
//    }
//}
