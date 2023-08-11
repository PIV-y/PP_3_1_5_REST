package ru.kata.spring.boot_security.demo.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.UserMan;

import java.util.List;

@Repository
@EnableTransactionManagement
public class UserDaoImp implements UserDao {
    private static final String HQL_DROP_USERS_TABLE = "DELETE FROM user_man";
    private static final String HQL_DROP_ROLES_TABLE = "DELETE FROM role";
    private static final String HQL_GET_USER_BY_ID = "SELECT u FROM UserMan u WHERE u.id = :id";
    private static final String HQL_GET_USER_BY_NAME = "SELECT u FROM UserMan u WHERE u.name = :NAME";
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
        System.out.println("в дао для удаления по id");
        entityManager.remove(getUserById(id));
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
        System.out.println("юзер в дао: " + userMan.toString() + " параметр id " + id);
        entityManager.merge(userMan);
    }
    @Override
    @Transactional
    public void saveRoleForUser (Role role) {
        entityManager.persist(role);
    }

    @Override
    @Transactional
    public UserMan getUserById(long id) {
        return (UserMan) entityManager.createQuery(HQL_GET_USER_BY_ID)
                .setParameter("id", id)
                .getSingleResult();
    }
    @Override
    public UserMan getUserByName(String userName) {
        return (UserMan) entityManager.createQuery(HQL_GET_USER_BY_NAME)
                .setParameter("NAME", userName)
                .getSingleResult();
    }

}
