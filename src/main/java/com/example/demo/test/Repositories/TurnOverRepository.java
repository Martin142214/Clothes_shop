package com.example.demo.test.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.models.entities.DailyTurnOver;

public interface TurnOverRepository extends MongoRepository<DailyTurnOver, String>{
    
}
