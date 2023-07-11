package com.example.demo.test.models.entities;

import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Roles_privileges")
public class Privilege {
 
    @Id
    public String id;

    public String name;

    public Collection<Role> roles;

    public Privilege() {

    }
    
    public Privilege(String name) {
        this.name = name;
    }
}
