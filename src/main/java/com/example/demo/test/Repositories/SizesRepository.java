package com.example.demo.test.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.test.models.classModels.Sizes;

@Repository
public interface SizesRepository extends MongoRepository<Sizes, String> {
    
}
