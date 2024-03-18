package com.gmail.kolyvas.hotelapp.util.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.gmail.kolyvas.hotelapp.service.CustomUserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity

public class CustomWebSecurity   {

  //  public BCrypt encryptUtil;

   // public final CustomUserDetailsService userDetailsService;

  /*  public CustomWebSecurity(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        encryptUtil = new BCrypt();

    }*/
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsService userService, BCryptPasswordEncoder bCrypt )
    throws Exception{
        AuthenticationManagerBuilder builder =  http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userService).passwordEncoder(bCrypt);
        return builder.build();

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,  JwtTokenFilter jwtTokenFilter) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->{
                    request.anyRequest().permitAll();//.anyRequest();

                }).csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();


    }
}
