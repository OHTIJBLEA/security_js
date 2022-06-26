package ru.kata.spring.boot_security.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.security.Principal;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @GetMapping("/user")
    public String showUser(Principal principal, Model model) {
        User user = (User) userService.loadUserByUsername(principal.getName());
        User user1 =  userService.findUserById(userService.getUsernameByName(principal.getName()));
        model.addAttribute("oneUser", user);
        return "/user";

    }

    @GetMapping("/user/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "user-update";
    }

    @PostMapping("/user/user-update")
    public String updateUsers(@ModelAttribute("user") User user, Principal principal) {
        User userDb = userService.findUserById(userService.getUsernameByName(principal.getName()));
        Set<Role> roles = userDb.getRoles();
        user.setRoles(roleService.getRoleByName(roles.stream().map(role-> role.getName()).toArray(String[]::new)));
        userService.saveUser(user);
        return "redirect:/user";
    }
}










//user.setRoles(roleService.getRoleByName(roles.stream().toArray(String[]::new)));
//    @PostMapping("/user/user-update")
//    public String updateUsers(@ModelAttribute("user") User user, Principal principal) {
////        Role role = roleService.getRoleByUsername(userRepository.findByUsername(principal.getName()));
//        User user1 = userService.findUserById(userService.getUsernameById(principal.getName()));
//        Set<Role> role = user1.getRoles();
//        System.out.println(role);
//        String roles = role.toString();
////        if (user.getRoles() == null) {
//        user.setRoles(roleService.getRoleByName(new String[role]));
////            user.setRoles(roleService.getRoleByName(new String[]{"ROLE_USER"}));
////        }
//        userService.saveUser(user);
//        return "redirect:/user";
//    }