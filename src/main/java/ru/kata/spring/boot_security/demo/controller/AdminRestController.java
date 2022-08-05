package ru.kata.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/admin")
    public List<User> allUsers() {
        List<User> users = userService.findAllUsers();
        return users;
    }

    @PostMapping("/admin/newUser")
    public User addUser(@RequestBody User user) {
        userService.saveUser(user);
        return user;
    }

    @PatchMapping("/admin/edit")
    public User updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return user;
    }

    @DeleteMapping("/admin/user/{id}")
    public void deleteUserById(@PathVariable("id") long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/roles")
    public List<Role> allRoles() {
        List<Role> roles = new ArrayList<>(roleService.getAllRoles());
        return roles;
    }
}
