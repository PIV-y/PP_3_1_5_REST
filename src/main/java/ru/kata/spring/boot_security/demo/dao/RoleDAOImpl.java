package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDAOImpl implements RoleDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean add(Role role) {
        entityManager.persist(role);
        return true;
    }
    @Override
    public List<Role> getList() {
        return entityManager.createQuery("select s from Role s", Role.class).getResultList();
    }

    @Override
    public Role getRole(Integer id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public void deleteRole(Integer id) {
        entityManager.remove(getRole(id));
    }

    @Override
    public void editRole(Role role) {
        entityManager.merge(role);
    }
    @Override
    public List<Role> listByName(List<String> name) {
        return  entityManager.createQuery("select u FROM Role u WHERe u.name in (:id)", Role.class)
                .setParameter("id", name)
                .getResultList();
    }
    @Override
    public Role findByName(String name) {
        return entityManager.createQuery("select u FROM Role u WHERe u.name = :id", Role.class)
                .setParameter("id", name)
                .getResultList().stream().findAny().orElse(null);
    }
    @Override
    public Role convert(String id) {
        Role role = new Role();
        role.setId(Long.valueOf(Integer.valueOf(id)));
        return role;
    }
}
