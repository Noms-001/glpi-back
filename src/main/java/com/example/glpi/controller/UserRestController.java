package com.example.glpi.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.glpi.entity.User;
import com.example.glpi.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserRestController {

    private final UserService userService;
    
    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }
}
