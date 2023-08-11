package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserMan;

import java.util.List;

public interface UserDao {

    void dropData();

    void saveUser(UserMan userMan);

    void removeUserById(long id);

    List<UserMan> getAllUsers();

    void changeByID(UserMan userMan, long id);

    UserMan getUserById(long id);

    public void saveRoleForUser (Role role);

    public UserMan getUserByName(String userName);

    }
