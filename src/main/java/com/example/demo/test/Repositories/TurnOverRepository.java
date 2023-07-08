package com.example.demo.test.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.Models.entities.DailyTurnOver;

public interface TurnOverRepository extends MongoRepository<DailyTurnOver, String>{
    
}
