package ru.kata.spring.boot_security.demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.UserMan;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Optional;

@Component
public class AuthProviderImp implements AuthenticationProvider {

    private UserService userService;

    public AuthProviderImp(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserMan user = userService.getUserByName(username);

        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("no confirmation");
        }
        return new UsernamePasswordAuthenticationToken(user, user.getRoles());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
