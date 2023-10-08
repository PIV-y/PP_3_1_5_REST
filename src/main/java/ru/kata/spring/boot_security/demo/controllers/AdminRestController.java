package ru.kata.spring.boot_security.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin")
public class AdminRestController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public AdminRestController (UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> showAllUsers () {
/*        model.addAttribute("admin", userService.findByEmail(principal.getName()));
        model.addAttribute("users", userService.getList());
        model.addAttribute("roles", roleService.getList());
        model.addAttribute("user", new User());
        return "all-users";*/
        return new ResponseEntity<>(userService.getList(), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
/*
    @GetMapping("/addNewUser")
    public ResponseEntity<User> addNewUser (@RequestBody User user) {
        User user = new User();
        model.addAttribute("user", user);

        List<Role> roles = roleService.getList();
        model.addAttribute("roleList", roles);
        return "user-info";
    }*/

    @PostMapping("/saveUser")
    public ResponseEntity<User> saveUser (@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

/*    @GetMapping("/update/{id}")
    public String updateUser (@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("user", userService.showUser(id));
        model.addAttribute("roleList",roleService.getList());
        return "update";
    }*/

    @PostMapping("/edit/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody User user) {
        userService.updateUser(id,user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
