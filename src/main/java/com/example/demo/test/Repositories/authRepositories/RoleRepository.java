package com.example.demo.test.repositories.authRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.models.entities.Role;

public interface RoleRepository extends MongoRepository<Role, String>{
    
    Role findByName(String name);
}
