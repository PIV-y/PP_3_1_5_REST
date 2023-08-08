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
                return "redirect:/users/{id}/read_profile";
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

// Обновление юзера в БД по введенным данным
    @PatchMapping ("/admin/users/{id}")
    public String update (@ModelAttribute("user") UserMan user,
                          @PathVariable("id") int id,
                          @RequestParam("roleName") String rolename) {
        Role newRole = new Role(rolename);
        if (!user.getRoles().contains(newRole)) {
            user.addRole(newRole);
        }
        userService.changeByID(user, id);
        return "redirect:/admin/users";
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

// Чтение профиля авторизированного пользователя
    @GetMapping("/users/{id}/read_profile")
    public String readProfileUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "profile-page";
    }
}