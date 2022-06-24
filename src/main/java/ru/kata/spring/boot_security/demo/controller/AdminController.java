package ru.kata.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;
    private final UserRepository userRepository;

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
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return "redirect:/admin";
        }

        if (user.getRoles() == null) {
            user.setRoles(Collections.singleton(new Role(2L)));
        }
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
        model.addAttribute("roles", roleService.gelAllRoles());
        model.addAttribute("user", userService.findById(id));
        return "admin-update";
    }

    @PostMapping("/admin/user-update")
    public String updateUsers(@ModelAttribute("user") User user,
                              @RequestParam(value = "nameRoles", required = false) String roles) {
        if(roles == null){
            user.setRoles(roleService.getRoleByName("ROLE_USER"));
        } else {
            user.setRoles(roleService.getRoleByName(roles));
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }
}
