package hac.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class AppConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder bCryptPasswordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("Ariel")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("Noam")
                .password(bCryptPasswordEncoder.encode("1234"))
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(bCryptPasswordEncoder.encode("12345"))
                .roles("ADMIN")
                .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
                .csrf(withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("static/**", "/css/**", "/403", "/errorpage","/", "/register", "/courses", "/About-Us").permitAll()
                        .requestMatchers("/admin/**", "/api/**").hasRole("ADMIN")
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
