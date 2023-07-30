package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    void dropData();

    void saveUser(User user);

    void removeUserById(long id);

    List<User> getAllUsers();

    void changeByID(User user, long id);

    User getUserById(long id);
}
