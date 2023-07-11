package com.example.demo.test.models.entities;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Favorites")
public class Favorite {

    @Id
    public String id;

    public Collection<Shoe> shoes;

    public Collection<Clothes> clothes;

    public User user;

    public Favorite() {}
}
