package com.example.login.config;

import com.example.login.model.UserRepository;
import com.example.login.security.CustomWebAuthenticationDetailsSource;
import com.example.login.security.TwoFactorAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   private final CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource;

   public SecurityConfig(CustomWebAuthenticationDetailsSource customWebAuthenticationDetailsSource){
      this.customWebAuthenticationDetailsSource = customWebAuthenticationDetailsSource;
   }
   @Bean
   public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
      return new TwoFactorAuthenticator(userRepository, userDetailsService, passwordEncoder);
   }

   @Bean
   public AuthenticationManager authManager(HttpSecurity httpSecurity, DaoAuthenticationProvider daoAuthenticationProvider) throws Exception {
       return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
              .authenticationProvider(daoAuthenticationProvider)
              .build();
   }
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
       http
               .csrf(csrf -> csrf.ignoringRequestMatchers( "/register" ))
               .authorizeHttpRequests(authorizeRequests ->
                       authorizeRequests
                               .requestMatchers("/register").permitAll()
                               .requestMatchers( "/homepage" ).permitAll()
                               .requestMatchers("/admin/**"  ).hasRole( "ADMIN" )
                               .requestMatchers( "user/**" ).hasRole( "USER" ))
               .formLogin(formLogin->
                       formLogin
                               .loginPage("/login")
                               .authenticationDetailsSource( customWebAuthenticationDetailsSource )
                               .defaultSuccessUrl("/homepage", true)
                               .failureUrl("/login?error=true")
                               .permitAll());
       return http.build();
   }

  /* @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
       var userDetailsService = new InMemoryUserDetailsManager();
       var user = User.builder()
               .username("user")
               .password(passwordEncoder().encode("password"))
               .roles("USER")
               .build();
       userDetailsService.createUser(user);


       var admin = User.builder()
               .username("admin")
               .password(passwordEncoder().encode("adminpassword"))
               .roles("ADMIN")
               .build();
       userDetailsService.createUser(admin);

       return userDetailsService;
   }*/

   @Bean
    public PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
   }



}