package com.example.demo.test.services;

import java.util.Collection;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.test.models.entities.Role;
import com.example.demo.test.models.entities.User;
import com.example.demo.test.repositories.authRepositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository _userRepository;
    
    
    public User getCurrentUser(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = _userRepository.findByEmail(user.getName());
        return currentUser;
    }

    public User getAdminUser() {
        var adminUserEntity = _userRepository.findAll()
                                             .stream()
                                             .filter(user -> user.roles.stream().allMatch(role -> role.name.equals("ROLE_ADMIN")))
                                             .findAny().get();
        return adminUserEntity;                                     
    }

    public Boolean isAdmin(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = user.getName();
        User userEntity = _userRepository.findByEmail(userEmail);
        for (Role role : userEntity.roles) {
            if (role.name.equals("ROLE_ADMIN")) {
                return true;
            }
            else if (role.name.equals("ROLE_USER")){
                return false;
            }
        }
        return false;
    }

    public Collection<User> getAllUsers(){
        var users = _userRepository.findAll()
                                   .stream()
                                   .filter(user -> user.roles.stream().allMatch(role -> role.name.equals("ROLE_USER")))
                                   .collect(Collectors.toList()); 
        return users;                                  
    }
}
