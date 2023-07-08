package com.example.demo.test.Repositories.authRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.Models.entities.Role;

public interface RoleRepository extends MongoRepository<Role, String>{
    
    
    Role findByName(String name);
}
