package ru.kata.spring.boot_security.demo.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.UserMan;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private UserService userService;


    public SuccessUserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        System.out.println("SuccessUserHandler: User successfully authenticated.");
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        System.out.println("usrName: " + username + "; psw: " + password + "; roles: " + roles );

        try {
            if (roles.contains("ROLE_ADMIN")) {
                System.out.println("Role is ADMIN, Welcome");
                httpServletResponse.sendRedirect("/admin/users");
            } else if (roles.contains("ROLE_USER")) {
                System.out.println("Role is USER, Welcome");
                httpServletResponse.sendRedirect("/users/read_profile");
            } else {
                System.out.println("фильтры по ролям не прошли");
                httpServletResponse.sendRedirect("/");
            }
        } catch (Exception e) {
            System.out.println("Не правильное имя или нет такого пользователя");
        }
    }
}