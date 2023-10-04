package ru.kata.spring.boot_security.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Component
public class UserServiceImpl implements UserService{

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User showUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> showUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
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

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<User> getList() {
        return userDAO.getList();
    }

    @Override
    public User findByEmail(String userName) {
        return userDAO.findByEmail(userName);
    }

    @Override
    @Transactional
    public void register(User user) {
        Role role1 = roleRepository.findById(1L).get();
        Role role2 = roleRepository.findById(2L).get();

        if (user.getOriginalUsername().equals("admin")) {
            user.addRole(role1);
        } else {
            user.addRole(role2);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
