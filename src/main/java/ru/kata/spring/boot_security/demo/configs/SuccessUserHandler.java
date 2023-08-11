package ru.kata.spring.boot_security.demo.configs;

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
    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
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
        // Проверка наличия роли "ROLE_USER" в списке ролей.
        System.out.println(roles);
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserMan userMan = null;
        try {
            userMan = userService.getUserByName(username);
            if (password.equals(userMan.getPassword()) && (roles.contains("ROLE_ADMIN"))) {
                httpServletResponse.sendRedirect("/admin/users");
            } else if (password.equals(userMan.getPassword()) && roles.contains("ROLE_USER")) {

                httpServletResponse.sendRedirect("/users/{id}/read_profile");
            } else {
                System.out.println("фильтры по ролям не прошли");
                httpServletResponse.sendRedirect("/");
            }







        } catch (NullPointerException e) {
            System.out.println("Не правильное имя или нет такого пользователя");
        }

        if (password.equals(userMan.getPassword()))


        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/admin/users");
        } else if (roles.contains("ROLE_USER")) {

            httpServletResponse.sendRedirect("/users/{id}/read_profile");
        } else {
            System.out.println("фильтры по ролям не прошли");
            httpServletResponse.sendRedirect("/");
        }
    }


}