package com.example.login.config;


import com.example.login.model.UserRepository;
import com.example.login.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * SecurityConfig är klassen som konfigurerar säkerheten för vår applikation.
 * Här ställer vi in hur inloggning, utloggning och tillgång till olika sidor ska fungera.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   /**
    * Skapar en AuthenticationManager som hanterar användarens inloggning med hjälp av deras detaljer och lösenord.
    */
   @Bean
   public AuthenticationManager authManager(HttpSecurity http, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
      return http.getSharedObject( AuthenticationManagerBuilder.class)
              .userDetailsService(userDetailsService)
              .passwordEncoder(passwordEncoder)
              .and()
              .build();
   }

   /**
    * Skapar en SecurityFilterChain som innehåller alla våra säkerhetsinställningar.
    */
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
              .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
              .authorizeHttpRequests(authorizeRequests ->
                      authorizeRequests
                              .requestMatchers("/login", "/logout", "/register", "/h2-console/**").permitAll()
                              .requestMatchers("/admin/**").hasRole("ADMIN")
                              .requestMatchers("/users", "/delete").hasRole("ADMIN")
                              .anyRequest().authenticated()
              )
              .formLogin(formLogin ->
                      formLogin
                              .loginPage("/login")
                              .defaultSuccessUrl("/homepage", true)
                              .failureUrl("/login?error=true")
                              .permitAll()
              )
              .logout(logout -> logout
                      .logoutUrl("/perform_logout")
                      .logoutSuccessUrl("/login")
                      .permitAll())
              .headers(headers -> headers.frameOptions().disable());

      return http.build();
   }
   /**
    * Skapar en PasswerdEncoder klass som använder BCrypt-algoritmen för att kryptera lösenord.
    */
   @Bean
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
   
   /**
    * Skapar en UserDetailsService som hanterar inloggningsinformation för användare.
    */
   @Bean
   public UserDetailsService userDetailsService(UserRepository userRepository) {
      return new MyUserDetailsService(userRepository);
   }
}
