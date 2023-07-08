package com.example.demo.test.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.test.Models.entities.Comment;

public interface CommentRepository extends MongoRepository<Comment, String> {
    
}
