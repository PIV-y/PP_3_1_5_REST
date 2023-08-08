package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.UserMan;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {
    private static final String HQL_DROP_USERS_TABLE = "DELETE FROM user_man";
    private static final String HQL_DROP_ROLES_TABLE = "DELETE FROM role";
    private static final String HQL_GET_USER_BY_ID = "SELECT u FROM UserMan u WHERE u.id = :id";
    private static final String HQL_REMOVE_USER_BY_ID = "DELETE FROM UserMan u WHERE u.id = :id";
    private static final String HQL_CHANGE_USER_BY_ID = "UPDATE UserMan u SET u.name=:name, " +
            "u.lastName=: lastname, u.age=:age, u.password=:password, u.roles=:role WHERE u.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void dropData() {
        entityManager.createNativeQuery(HQL_DROP_ROLES_TABLE).executeUpdate();
        System.out.println("ROLES успешно удалены");
        entityManager.createNativeQuery(HQL_DROP_USERS_TABLE).executeUpdate();

        entityManager.createNativeQuery("ALTER TABLE user_man AUTO_INCREMENT = 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE role AUTO_INCREMENT = 1").executeUpdate();
        System.out.println("в дао удаление прошло успешно");
    }

    @Override
    public void saveUser(UserMan userMan) {
        entityManager.persist(userMan);
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
    public List<UserMan> getAllUsers() {
        List<UserMan> userMEN = entityManager.createQuery("from UserMan", UserMan.class).getResultList();
        return userMEN;
    }

    @Override
    @Transactional
    public void changeByID(UserMan userMan, long id) {
        entityManager.createQuery(HQL_CHANGE_USER_BY_ID)
                .setParameter("id", id)
                .setParameter("name", userMan.getName())
                .setParameter("lastname", userMan.getLastName())
                .setParameter("age", userMan.getAge())
                .setParameter("password", userMan.getPassword())
                .setParameter("role", userMan.getRoles())
                .executeUpdate();
    }

    @Override
    @Transactional
    public UserMan getUserById(long id) {
        return (UserMan) entityManager.createQuery(HQL_GET_USER_BY_ID)
                .setParameter("id", id)
                .getSingleResult();
    }
}
