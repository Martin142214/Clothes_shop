package com.example.demo.test.models.cartModels;

import com.example.demo.test.models.enums.Brands;
import com.example.demo.test.models.enums.Conditions;

public class CartShoeModel {
    
    public String id;
    public Brands brand;
    public String model;
    public String releaseDate;
    public String size;
    public Conditions condition;
    public String colorSpecification;
    public Integer price;
    public boolean isAuctionOffer;
    public String mainImageUrl;

    public CartShoeModel() {
        
    }
}
