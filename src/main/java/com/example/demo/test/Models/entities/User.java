package com.example.demo.test.Models.entities;

import java.util.Collection;
import javax.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;

    @Column(nullable = false, unique = true, length = 20)
    public String username;

    @Column(nullable = false, unique = true, length = 45)
    public String email;

    @Column(nullable = false, length = 64)
    public String password;

    public boolean enabled;
    public boolean tokenExpired;

    public Collection<Role> roles;

    public User() {
        
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public boolean isEnabled(){
        if (enabled) {
            return true;
        }
        else {
            return false;
        }
    }

  /*public List<String> getRoles(){
      return roles;
  }

  public void setRoles(List<String> roles){
      this.roles = roles;
  }*/
    
}
