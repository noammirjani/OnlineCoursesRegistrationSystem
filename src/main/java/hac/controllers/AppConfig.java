package hac.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class AppConfig {

    /**
     * Creates and configures the user details service.
     *
     * @param bCryptPasswordEncoder The password encoder for encoding passwords.
     * @return The configured user details service.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("Ariel@hac.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("Noam@hac.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("Admin@hac.com")
                .password(bCryptPasswordEncoder.encode("12345"))
                .roles("ADMIN")
                .build());
        return manager;
    }

    /**
     * Creates and configures the password encoder.
     *
     * @return The configured password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for HTTP security.
     *
     * @param http The HTTP security object to be configured.
     * @return The configured security filter chain.
     * @throws Exception If an exception occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
                .csrf(withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("static/**", "/css/**", "/fragments/**", "/403", "/login",
                                                   "/error-page","/", "/courses", "/about-us", "/home-page").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER"))
                .formLogin((form) -> form
                                 .loginPage("/login")
//                               .loginProcessingUrl("/perform_login")
//                               .defaultSuccessUrl("/", true)
                                 .failureUrl("/login?error=true")
                                .permitAll())
                .logout((logout) -> logout.permitAll())
                .exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/403"));
                return http.build();
    }
}
