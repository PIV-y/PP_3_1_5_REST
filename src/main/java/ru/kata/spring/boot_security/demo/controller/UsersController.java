package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UsersController {
    private UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

// Стартовая страница
    @GetMapping()
    public String printStart (Model model) {
        model.addAttribute("messages", "HEllO");
        return "start";
    }

// Получить список Пользователей GET
    @GetMapping("/users")
    public String printUserList (Model model) {
        model.addAttribute("user", userService.getAllUsers());
        return "users";
    }

// Добавить пользователя POST
    @PostMapping("/users")
    public String saveUser (@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

// Переход на форму создания нового юзера GET
    @GetMapping("/users/new")
    public String addNewUserInfo (Model model) {
        model.addAttribute("user", new User());
    return "user-info";
    }

// Получение юзера по ID для редактирования
    @PostMapping ("/users/{id}/edit")
    public String editUser(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        System.out.println(model.toString());
        return "edit";
    }

// Обновление юзера в БД по введенным данным
    @PatchMapping ("/users/{id}")
    public String update (@ModelAttribute("user") User user, @PathVariable("id") int id) {
        System.out.println("Обновленный юзер пришел: " + user.toString());
        System.out.println("id пришел в контроллер: " + id);
        userService.changeByID(user, id);
    //    userService.changeByID(user);
        System.out.println("юзер ушел в БД");
        return "redirect:/users";
    }

// Очистить полностью таблицу юзеров
    @DeleteMapping("/users")
    public String deleteAllUsers () {
        userService.dropData();
        return  "redirect:/users";
    }

// Очистить таблицу юзеров по ID
    @DeleteMapping("/users/{id}/delete")
    public String deleteUserByID (@RequestParam("id") long id) {
        userService.removeUserById(id);
        return "redirect:/users";
    }


}