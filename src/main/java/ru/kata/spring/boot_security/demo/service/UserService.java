package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    List<User> findAll();
    void deleteById(Long id);

    UserDetails loadUserByUsername(String username);
    User findUserById(Long userId);
    boolean saveUser(User user);
    boolean updateUser(User user);
    Long getUsernameByName(String name);
}
