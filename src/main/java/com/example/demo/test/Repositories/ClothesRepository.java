package com.example.demo.test.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.test.Models.entities.Clothes;

@Repository
public interface ClothesRepository extends MongoRepository<Clothes, String> {
    
}
