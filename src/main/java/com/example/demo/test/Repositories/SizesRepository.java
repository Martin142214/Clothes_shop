package com.example.demo.test.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.test.Models.classModels.Sizes;

@Repository
public interface SizesRepository extends MongoRepository<Sizes, String> {
    
}
