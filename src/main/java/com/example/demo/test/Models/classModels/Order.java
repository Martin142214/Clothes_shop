package com.example.demo.test.models.classModels;

public class Order {

    public Integer orderNumber;

    public String username;

    public Integer priceSummaryFromOrder;

    public Order() {
        
    }

    public Order(Integer orderNumber, String username, Integer priceSummaryFromOrder) {
        this.orderNumber = orderNumber;
        this.username = username;
        this.priceSummaryFromOrder = priceSummaryFromOrder;
    }
}
