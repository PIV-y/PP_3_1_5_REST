package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/")
public class UsersController {
    private UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String printStart (Model model) {
        model.addAttribute("messages", "HEllO");
        return "start";
    }
    @GetMapping("/user_list")
    public String printUserList (Model model) {
        model.addAttribute("user", userService.getAllUsers());
        System.out.println(model.toString());
        return "users";
    }

    @GetMapping("/user_info")
    public String addNewUserInfo (Model model) {
        model.addAttribute("user", new User());
        System.out.println("форма открылась");
    return "user-info";
    }
    @PostMapping("/save_user")
    public String saveUser (@ModelAttribute("user") User user) {
        userService.saveUser(user);
        System.out.println("user added!!! "
                + user.getName() + user.toString());
        return "redirect:/user_list";
    }

    @PostMapping ("/{id}/update")
    public String editUser(Model model, @RequestParam("id") int id) {
        model.addAttribute("user", userService.getUserById(id));
        System.out.println(model.toString());
        return "edit";
    }

    @GetMapping("/clean_table")
    public String deleteAllUsers () {
        userService.dropData();
        return  "redirect:/user_list";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUserByID (@RequestParam("id") long id) {
        userService.removeUserById(id);
        return "redirect:/user_list";
    }

    @PatchMapping ("/change")
    public String update (@ModelAttribute("user") User user) {
        System.out.println("Обновленный юзер пришел: " + user.toString());
        userService.changeByID(user);
        System.out.println("юзер ушел в БД");
        return "redirect:/user_list";
    }
}