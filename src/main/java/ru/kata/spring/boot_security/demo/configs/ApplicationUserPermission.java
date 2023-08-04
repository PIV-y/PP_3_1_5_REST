package ru.kata.spring.boot_security.demo.configs;

public enum ApplicationUserPermission {
    USER_VIEW("user:view"),
    USER_EDIT("user:edit");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
