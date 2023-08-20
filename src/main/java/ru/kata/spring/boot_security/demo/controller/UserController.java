package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserMan;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

// Стартовая страница
    @GetMapping("/")
    public String printStart (Model model) {
        model.addAttribute("messages", "HEllO");
        return "start";
    }

// Сортировочный метод
    @GetMapping("/sort")
    public String printMyPage (Model model) {
        //Объект Authentication в Spring Security содержит информацию об аутентификации пользователя.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        System.out.println("принципал :" + authentication.getPrincipal());
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
                System.out.println("Hello Admin!");
                return "redirect:/admin/users";
            } else if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
                System.out.println("Hello User!");
                return "redirect:/users/read_profile";
            }
        } else {
            System.out.println("фильтры контроллера не пройдены!");
        }
        return "redirect:/";
    }

// Получить список Пользователей GET
    @GetMapping("/admin/users")
    public String printUserList (Model model) {
        model.addAttribute("user", userService.getAllUsers());
        return "users";
    }

// Добавить пользователя POST
    @PostMapping("/admin/users")
    public String saveUser(@ModelAttribute("user") UserMan user, @RequestParam("roleName") String roleName) {
        user.setRoles(Arrays.asList(new Role(roleName)));
        userService.saveUser(user);
        System.out.println(user.toString());
        return "redirect:/admin/users";
    }

// Переход на форму создания нового юзера GET
    @GetMapping("/admin/users/new")
    public String addNewUserInfo (Model model) {
        model.addAttribute("user", new UserMan());
    return "add new user";
    }

// Переход на форму создания нового юзера GET
    @GetMapping("/users/new")
    public String signUp (Model model) {
        model.addAttribute("user", new UserMan());
        return "sign up";
    }

// Получение юзера по ID для редактирования
    @PostMapping ("/admin/users/{id}/edit")
    public String editUser(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }
    @GetMapping ("/users/edit")
    public String editUser(Model model,Authentication authentication) {
        model.addAttribute("user", userService.getUserByName(authentication.getName()));
        return "edit_by_user";
    }

// Обновление юзера в БД по введенным данным
    @PatchMapping ("/admin/users/{id}")
    public String update (@ModelAttribute("user") UserMan user,
                          @PathVariable("id") int id,
                          @RequestParam("roleName") String roleName) {
        UserMan existingUser = userService.getUserById(id);
        Role additionalRole = new Role(roleName);
        if (existingUser == null) {
            System.out.println("нет такого юзера в базе");
            return "redirect:/admin/users";
        }

        if (roleName.equals("ROLE_ADMIN") || roleName.equals("ROLE_USER")) {
            additionalRole.setUser(existingUser);
            userService.saveRoleForUser(additionalRole);
        } else {
            System.out.println("нет такой роли");
            return "redirect:/admin/users";
        }
        existingUser.setName(user.getName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setPassword(user.getPassword());
        existingUser.getRoles().add(additionalRole);

        System.out.println("до отправки в БД: " + user.toString());
        userService.changeByID(existingUser, id);
        System.out.println("пришел с дао в контроллер");
        return "redirect:/admin/users";
    }
    // Обновление юзера в БД по введенным данным
    @PatchMapping ("/users")
    public String update (@ModelAttribute("user") UserMan user,
                          Authentication authentication) {
        UserMan existingUser = userService.getUserByName(authentication.getName());
        if (existingUser == null) {
            System.out.println("нет такого юзера в базе");
            return "redirect:/admin/users";
        }

        existingUser.setName(user.getName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setPassword(user.getPassword());

        System.out.println("до отправки в БД: " + user.toString());
        userService.changeByID(existingUser, existingUser.getId());
        System.out.println("пришел с дао в контроллер");
        return "redirect:/users/read_profile";
    }
// Очистить полностью таблицу юзеров
    @DeleteMapping("/admin/users")
    public String deleteAllUsers () {
        userService.dropData();
        System.out.println("удаление отработано!");
        return  "redirect:/admin/users";
    }

// Очистить таблицу юзеров по ID
    @DeleteMapping("/admin/users/{id}/delete")
    public String deleteUserByID (@RequestParam("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }
    @GetMapping("/users/delete")
    public String deleteUserByID (Authentication authentication) {
        userService.removeUserById(userService.getUserByName(authentication.getName()).getId());
        return "redirect:/";
    }

// Чтение профиля авторизированного пользователя
    @GetMapping("/users/read_profile")
    public String readProfileUser(Model model, Authentication authentication) {
        model.addAttribute("user", userService.getUserByName(authentication.getName()));
        return "profile-page";
    }
}