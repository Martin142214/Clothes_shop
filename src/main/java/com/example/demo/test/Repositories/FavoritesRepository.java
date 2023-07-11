package com.example.demo.test.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.models.entities.Favorite;

public interface FavoritesRepository extends MongoRepository<Favorite, String>{
    
}
