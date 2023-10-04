package ru.kata.spring.boot_security.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserDetailsServiceApp;

@Component
public class UserValidator implements Validator {

    private final UserDetailsServiceApp userDetailsServiceApp;
    private static final Logger log = LoggerFactory.getLogger(UserValidator.class);

    @Autowired
    public UserValidator(UserDetailsServiceApp userDetailsServiceApp) {
        this.userDetailsServiceApp = userDetailsServiceApp;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        log.info("user зашел в валидатор: " + user.toString());

        try {
            userDetailsServiceApp.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("username","","Такой пользователь уже существует");
    }
}
