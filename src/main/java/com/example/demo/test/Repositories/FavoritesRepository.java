package com.example.demo.test.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.Models.entities.Favorite;

public interface FavoritesRepository extends MongoRepository<Favorite, String>{
    
}
