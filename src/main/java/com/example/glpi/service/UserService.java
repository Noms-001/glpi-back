package com.example.glpi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.glpi.entity.User;
import com.example.glpi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }
}