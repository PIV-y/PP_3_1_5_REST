package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.AdminDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
public class AdminServiceImp implements AdminService {
    private AdminDao adminDao;

    public AdminServiceImp(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    @Transactional
    public void dropData() {
        adminDao.dropData();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        adminDao.saveUser(user);
    }

    @Override
    @Transactional
    public void removeUserById(long id) {
        adminDao.removeUserById(id);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return adminDao.getAllUsers();
    }

    @Override
    @Transactional
    public void changeByID(User user, long id) {
        adminDao.changeByID(user, id);
    }

    @Override
    @Transactional
    public User getUserById(long id) {
        return adminDao.getUserById(id);
    }
}
