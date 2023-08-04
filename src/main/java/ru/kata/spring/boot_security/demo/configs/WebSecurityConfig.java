package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // метод начинает конфигурирование правил авторизации для запросов.
                .authorizeRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
//                .requestMatchers("/users").hasRole("ADMIN")
                .requestMatchers("/users").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
   //             .requestMatchers("/admin/").hasRole("ADMIN")
                .and()
                .formLogin().successHandler(successUserHandler).permitAll()
                .and()
                .logout(logOutPage -> logOutPage.logoutSuccessUrl("/").permitAll());
        return http.build();
    }
// In-Memory
    @Bean
    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user")
//                        .roles("USER")
//                        .build();
        UserDetails admin =
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("admin")
                        .roles("ADMIN")
                        .build();
        return new InMemoryUserDetailsManager(admin);
    }

    //jdbc-authentication
//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource) {
//        UserDetails user =
//            User.withDefaultPasswordEncoder()
//                    .username("user")
//                    .password("user")
//                    .roles("USER")
//                    .build();
//        UserDetails admin =
//            User.withDefaultPasswordEncoder()
//                    .username("admin")
//                    .password("admin")
//                    .roles("USER","ADMIN")
//                    .build();
//
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        if (users.userExists(user.getUsername())) {
//            users.deleteUser(user.getUsername());
//        }
//        if (users.userExists(admin.getUsername())) {
//            users.deleteUser(admin.getUsername());
//        }
//        users.createUser(user);
//        users.createUser(admin);
//
//        return users;
//    }

}