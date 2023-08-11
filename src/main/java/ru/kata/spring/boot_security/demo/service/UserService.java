package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserMan;

import java.util.List;

public interface UserService {

    void dropData();

    void saveUser(UserMan userMan);

    void removeUserById(long id);

    List<UserMan> getAllUsers();

    void changeByID(UserMan userMan, long id);

    UserMan getUserById(long id);
    public UserMan getUserByName(String userName);

    public void saveRoleForUser (Role role);

}
