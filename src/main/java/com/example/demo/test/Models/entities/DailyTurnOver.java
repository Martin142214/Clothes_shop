package com.example.demo.test.Models.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("Turn_over")
public class DailyTurnOver {
    @Id
    public String id;

    public List<Order> orders;

    public Date date;
    
    public DailyTurnOver() {
        orders = new ArrayList<Order>();
    }
}
