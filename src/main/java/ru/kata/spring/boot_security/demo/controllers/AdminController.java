package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.AdminServiceImpl;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminServiceImpl adminServiceImpl;

    private final UserService userService;

    private final RoleService roleService;


    @Autowired
    public AdminController(AdminServiceImpl adminServiceImpl, UserService userService, RoleService roleService) {
        this.adminServiceImpl = adminServiceImpl;
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

    @GetMapping ("/delete/{id}")
    public String deleteUser (@PathVariable(value = "id") Long id) {
        adminServiceImpl.deleteUserById(id);
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
        adminServiceImpl.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/update/{id}")
    public String updateUser (@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", adminServiceImpl.showUser(id));
        model.addAttribute("roleList",roleService.getList());
        return "update";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable(value = "id") Long id) {
        adminServiceImpl.updateUser(id,user);
        return "redirect:/admin/";
    }
}
