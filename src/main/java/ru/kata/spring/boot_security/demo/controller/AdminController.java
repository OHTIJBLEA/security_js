package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;


    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> user = userService.findAll();
        model.addAttribute("allUser", user);
        return "admin";
    }

    @GetMapping("/admin/user-save")
    public String saveUserForm(User user) {
        return "admin-save";
    }

    @PostMapping("/admin/user-save")
    public String saveUser(User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User users = userService.findById(id);
        users.setRoles(roleService.gelAllRoles());
        model.addAttribute("user", users);
        return "admin-update";
    }

    @PostMapping("/admin/user-update")
    public String updateUsers(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
}
