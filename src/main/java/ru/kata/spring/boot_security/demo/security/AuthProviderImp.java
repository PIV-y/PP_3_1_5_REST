package ru.kata.spring.boot_security.demo.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.UserMan;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Component
public class AuthProviderImp implements AuthenticationProvider {

    private UserService userService;
    @PersistenceContext
    private EntityManager entityManager;


    public AuthProviderImp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String nameFromAuth = authentication.getName();
        String pswrdFromAuth = authentication.getCredentials().toString();

        UserMan userForAuth = entityManager.find(UserMan.class ,userService.getUserByName(nameFromAuth).getId());

        if (!pswrdFromAuth.equals(userService.getUserByName(nameFromAuth).getPassword())) {
            throw new BadCredentialsException("no confirmation");
        }

        System.out.println("principals:  " + userForAuth);
        System.out.println("pswrd:  " + pswrdFromAuth);
//        Collection<? extends GrantedAuthority> auth = userService.getUserByName(nameFromAuth).getAuthorities();
        Collection<? extends GrantedAuthority> auth = userForAuth.getRoles();
        System.out.println("Roles: " + auth.toString());

        return new UsernamePasswordAuthenticationToken(userForAuth, pswrdFromAuth, auth);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
