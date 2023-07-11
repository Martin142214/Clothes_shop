package com.example.demo.test.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.models.entities.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
    
}
