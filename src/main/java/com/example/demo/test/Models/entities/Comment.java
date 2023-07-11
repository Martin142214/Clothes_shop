package com.example.demo.test.models.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Comments")
public class Comment {
    @Id
    public String id;

    public String shoeId;

    public String username;

    public String content;

    public Date date;

    public Comment() {

    }

    public Comment(String shoeId, String username, String content, Date date) {
        this.shoeId = shoeId;
        this.username = username;
        this.content = content;
        this.date = date;
    }
}
