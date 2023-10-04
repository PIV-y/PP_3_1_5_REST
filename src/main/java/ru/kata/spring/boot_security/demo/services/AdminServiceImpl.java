package ru.kata.spring.boot_security.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.controllers.AdminController;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> showUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User showUser(Long id) {
        return userRepository.findById(id).get();
    }

    public User findByUsername (String name) {
       return userRepository.findByUsername(name);
    }

    @Override
    public void changeUserRole(Role role) {

    }

    @Transactional
    public void updateUser(Long id, User user) {
        User userToBeUpdated = showUser(id);
        log.info("user для обновления в АдминСервисе по id: " + userToBeUpdated.toString());
        log.info("user из аргументов в uodate в АдминСервисе : " + user.toString());
        userToBeUpdated.setUsername(user.getOriginalUsername());
        log.info("измененное имя у userToBeUpdated АдминСервисе : " + userToBeUpdated.getUsername());
        userToBeUpdated.setLastName(user.getLastName());
        userToBeUpdated.setEmail(user.getEmail());
        userToBeUpdated.setAge(user.getAge());
        userToBeUpdated.setPassword(passwordEncoder.encode(user.getPassword()));
        userToBeUpdated.setRoles(user.getRoles());
        log.info("user после внесения изменений: " + userToBeUpdated.toString());
        userRepository.save(userToBeUpdated);
    }


}
