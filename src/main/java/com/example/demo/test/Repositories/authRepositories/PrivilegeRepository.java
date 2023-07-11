package com.example.demo.test.repositories.authRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.models.entities.Privilege;

public interface PrivilegeRepository extends MongoRepository<Privilege, String> {
    
    Privilege findByName(String name);
}
