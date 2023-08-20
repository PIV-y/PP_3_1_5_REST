package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserMan;
import ru.kata.spring.boot_security.demo.service.UserService;

@Configuration
public class DataInitializer {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @EventListener
    public void onApplicationEvent (ContextRefreshedEvent event) {
        initializeData();
    }
    private void initializeData() {
        try {
            UserMan admin = userService.getUserByName("admin");
            admin.getRoles().size();
        } catch (EmptyResultDataAccessException e) {
            System.out.println("no admin found!");
            UserMan adminUser = new UserMan("admin", "admin", 30, "123");
            Role adminRole = new Role("ROLE_ADMIN");
            userService.saveUser(adminUser);
            userService.saveRoleForUser(adminRole);

            adminUser.getRoles().add(adminRole);
            adminRole.setUser(adminUser);

            userService.changeByID(adminUser, adminUser.getId());
        }
    }
}
