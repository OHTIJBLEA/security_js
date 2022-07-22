package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    User findUserById(Long id);
    List<User> findAllUsers();
    void deleteUserById(Long id);
    UserDetails loadUserByUsername(String username);
    boolean saveUser(User user);
    boolean updateUser(User user);
    Long getUsernameByName(String name);
    public boolean saveUserTest(User user);
}
