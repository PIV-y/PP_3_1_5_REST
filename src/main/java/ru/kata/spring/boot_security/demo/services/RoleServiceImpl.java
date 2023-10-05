package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.List;

@Component
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public boolean add(Role role) {
        roleRepository.save(role);
        return true;
    }

    @Override
    @Transactional
    public List<Role> getList() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public Role getRole(Long id) {
        return roleRepository.getById(id);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void editRole(Role role) {
        roleRepository.save(role);
    }
}