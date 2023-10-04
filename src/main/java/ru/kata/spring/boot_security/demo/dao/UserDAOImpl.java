package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        List<User> userList = entityManager.createQuery("from User").getResultList();
        return userList;
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    public User showUser (Long id) {
        return entityManager.find(User.class,id);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public void delete(Long id) {
        entityManager.remove(showUser(id));
    }

    @Transactional
    public User findUserByEmail(String email) {
        Query query = entityManager.createQuery("from User where email=:email", User.class);
        query.setParameter("email", email);
        return (User) query.getSingleResult();
    }

    @Override
    public List<User> getList() {
        return entityManager.createQuery("select distinct u from User u left join fetch u.roles ", User.class)
                .getResultList();
    }

    @Override
    public User findByEmail(String email) {
        return entityManager.createQuery("select xxx FROM User xxx JOIn fETCH xxx.roles WHERe xxx.email = :id", User.class)
                .setParameter("id", email)
                .getResultList().stream().findAny().orElse(null);
    }
}
