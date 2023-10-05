package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    boolean add(Role role);

    List<Role> getList();

    Role getRole(Long id);

    void deleteRole(Long id);

    void editRole(Role role);
}
