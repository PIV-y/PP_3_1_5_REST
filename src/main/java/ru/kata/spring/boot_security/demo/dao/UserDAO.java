package ru.kata.spring.boot_security.demo.dao;



import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDAO {
    List<User> getAllUsers();

    void saveUser(User user);

    User showUser (Long id);

    void update(User user);

    void delete(Long id);

    List<User> getList();

    User findByEmail(String userName);
}
