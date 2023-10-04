package ru.kata.spring.boot_security.demo.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(SuccessUserHandler.class);

    // Spring Security использует объект Authentication, пользователя авторизованной сессии.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        log.info("Получение ролей для аунтентификации...");
//        if (roles.contains("ROLE_ADMIN")) {
        if (roles.contains("ADMIN")) {
            log.info("Админ, добро пожаловать...");
            httpServletResponse.sendRedirect("/admin/");
//        } else if (roles.contains("ROLE_USER")) {
        } else if (roles.contains("USER")) {
            log.info("User, добро пожаловать...");
            httpServletResponse.sendRedirect("/user");
        }
        else {
            log.info("Роль не определена...");
            httpServletResponse.sendRedirect("/");
        }
    }
}