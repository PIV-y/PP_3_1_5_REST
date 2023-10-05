package ru.kata.spring.boot_security.demo.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/")
    public String showAllUsers (Model model, Principal principal) {
        model.addAttribute("admin", userService.findByEmail(principal.getName()));
        model.addAttribute("users", userService.getList());
        model.addAttribute("roles", roleService.getList());
        model.addAttribute("user", new User());;
        return "all-users";
    }

    @DeleteMapping ("/delete/{id}")
    public String deleteUser (@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/";
    }

    @GetMapping("/addNewUser")
    public String addNewUser (Model model) {
        User user = new User();
        model.addAttribute("user", user);

        List<Role> roles = roleService.getList();
        model.addAttribute("roleList", roles);
        return "user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser (@ModelAttribute("user") User user) {
        userService.saveUser(user);
        log.info("user пришедший в saveUser: " + user.toString());
        return "redirect:/admin/";
    }

    @GetMapping("/update/{id}")
    public String updateUser (@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.showUser(id));
        model.addAttribute("roleList",roleService.getList());
        return "update";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable(value = "id") Long id) {
        log.info("user на контроллере post update пришедший из вью: " + user.toString());
        log.info("роль юзера на контроллере post update: " + user.getRoles().toString());
        userService.updateUser(id,user);
        return "redirect:/admin/";
    }
}