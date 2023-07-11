package com.example.demo.test.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.test.models.entities.Clothes;

@Repository
public interface ClothesRepository extends MongoRepository<Clothes, String> {
    
}
