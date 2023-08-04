package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/")
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

/*// Получить список Пользователей GET
    @GetMapping("/admin/users")
    public String printUserList (Model model) {
        model.addAttribute("user", userService.getAllUsers());
        return "users";
    }*/
    // Получить страницу пользователя
    @GetMapping("/users")
    public String printMyPage (Model model) {
        //Объект Authentication в Spring Security содержит информацию об аутентификации пользователя.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication);
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
                return "users";
            } else {
                return "my page";
            }
        }
        System.out.println("no authntication!");
        model.addAttribute("user", userService.getAllUsers());
        return "my page";
    }

// Добавить пользователя POST
    @PostMapping("/admin/users")
    public String saveUser (@ModelAttribute("user") User user) {
        user.setRoles(new Role("guest"));
        userService.saveUser(user);
        System.out.println(user.toString());
        return "redirect:/admin/users";
    }

// Переход на форму создания нового юзера GET
    @GetMapping("/admin/users/new")
    public String addNewUserInfo (Model model) {
        model.addAttribute("user", new User());
    return "add user";
    }

// Получение юзера по ID для редактирования
    @PostMapping ("/admin/users/{id}/edit")
    public String editUser(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

// Обновление юзера в БД по введенным данным
    @PatchMapping ("/admin/users/{id}")
    public String update (@ModelAttribute("user") User user,
                          @ModelAttribute("role") Role role, @PathVariable("id") int id) {
        userService.changeByID(user, id);
        return "redirect:/users";
    }

// Очистить полностью таблицу юзеров
    @DeleteMapping("/admin/users")
    public String deleteAllUsers () {
        userService.dropData();
        return  "redirect:/users";
    }

// Очистить таблицу юзеров по ID
    @DeleteMapping("/admin/users/{id}/delete")
    public String deleteUserByID (@RequestParam("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin/users";
    }

// Чтение профиля авторизированного пользователя
    @GetMapping("users/{id}/read_profile")
    public String readProfileUser(Model model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        return "profile-page";
    }
}