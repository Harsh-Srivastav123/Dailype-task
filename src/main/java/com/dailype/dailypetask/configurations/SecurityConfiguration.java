package com.dailype.dailypetask.configurations;

import com.dailype.dailypetask.security.JwtAuthenticationException;
import com.dailype.dailypetask.security.JwtFilter;
import com.dailype.dailypetask.services.UserDetailsSecuredInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    JwtAuthenticationException point;

    @Bean
    public UserDetailsService userDetailsService(){
//        UserDetails user1= User.withUsername("harsh").password(passwordEncoder.encode("Pwd1")).roles("ADMIN","USER-1").build();
//        UserDetails user2= User.withUsername("raj").password(passwordEncoder.encode("Pwd2")).roles("USER-1").build();
//        return new InMemoryUserDetailsManager(user1,user2);
        return new UserDetailsSecuredInfoService();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/secured/create_user","/secured/verifyRegistration","/secured/auth","/create_user","/get_users","/delete_user"," /update_user")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(ex->ex.authenticationEntryPoint(point))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);// Enable filter

        return http.build();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
