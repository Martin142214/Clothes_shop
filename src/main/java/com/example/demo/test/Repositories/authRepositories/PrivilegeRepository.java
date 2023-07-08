package com.example.demo.test.Repositories.authRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.Models.entities.Privilege;

public interface PrivilegeRepository extends MongoRepository<Privilege, String> {
    
    Privilege findByName(String name);
}
