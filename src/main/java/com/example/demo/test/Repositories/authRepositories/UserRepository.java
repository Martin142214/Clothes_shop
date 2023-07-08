package com.example.demo.test.Repositories.authRepositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.Models.entities.User;

public interface UserRepository extends MongoRepository<User, String>{
    
    //@Query("SELECT u FROM User u WHERE u.username = ?1")
    //@Query("{'username':?0}")
    //add
    User findByUsername(String username);
    
    User findByEmail(String email);
}
