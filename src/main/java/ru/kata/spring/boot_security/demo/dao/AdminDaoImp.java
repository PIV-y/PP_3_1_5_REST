package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Repository
public class AdminDaoImp implements AdminDao {
    private static final String HQL_DROP_USERS_TABLE = "TRUNCATE TABLE User";
    private static final String HQL_GET_USER_BY_ID = "SELECT u FROM User u WHERE u.id = :id";
    private static final String HQL_REMOVE_USER_BY_ID = "DELETE FROM User u WHERE u.id = :id";
    private static final String HQL_CHANGE_USER_BY_ID = "UPDATE User u SET u.name=:name, u.lastName=: lastname, u.age=:age WHERE u.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void dropData() {
        entityManager.createNativeQuery(HQL_DROP_USERS_TABLE).executeUpdate();
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
        entityManager.close();
    }

    @Override
    @Transactional
    public void removeUserById(long id) {
        entityManager.createQuery(HQL_REMOVE_USER_BY_ID)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        List<User> users = entityManager.createQuery("from User", User.class).getResultList();
        return users;
    }

    @Override
    @Transactional
    public void changeByID(User user, long id) {
        entityManager.createQuery(HQL_CHANGE_USER_BY_ID)
                .setParameter("id", id)
                .setParameter("name", user.getName())
                .setParameter("lastname", user.getLastName())
                .setParameter("age", user.getAge())
                .executeUpdate();
        System.out.println("пришел в DAO: " + user.toString());
        System.out.println("Получили из БД: " + getUserById(user.getId()).toString());
    }

    @Override
    @Transactional
    public User getUserById(long id) {
        return (User) entityManager.createQuery(HQL_GET_USER_BY_ID)
                .setParameter("id", id)
                .getSingleResult();
    }
}
