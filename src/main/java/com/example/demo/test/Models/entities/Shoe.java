package com.example.demo.test.models.entities;


import java.io.Serializable;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.test.models.classModels.FileDB;
import com.example.demo.test.models.classModels.Sizes;
import com.example.demo.test.models.enums.Brands;
import com.example.demo.test.models.enums.Colors;
import com.example.demo.test.models.enums.Conditions;

@Document("Shoes")
public class Shoe implements Serializable {
    
    @Id
    public String id;
    public Brands brand;
    public String model;
    public String description;
    public String releaseDate;
    public List<Sizes> sizes;
    public Integer rating;
    public Conditions condition;
    public Colors color;
    public String colorSpecification;
    public Integer price;
    public boolean isAuctionOffer;
    public String mainImageUrl;
    public List<FileDB> sliderImages;

    public Shoe(Brands brand, String model, String description, String releaseDate, List<Sizes> sizes, Integer rating,
            Conditions condition, Colors color, String colorSpecificatiion, Integer price, boolean isAuctionOffer, String mainImageUrl, List<FileDB> sliderImages) {
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.releaseDate = releaseDate;
        this.sizes = sizes;
        this.rating = rating;
        this.condition = condition;
        this.color = color;
        this.colorSpecification = colorSpecificatiion;
        this.price = price;
        this.isAuctionOffer = isAuctionOffer;
        this.mainImageUrl = mainImageUrl;
        this.sliderImages = sliderImages;
    }

    public Shoe(Brands brand, String model, Integer price) {
        this.brand = brand;
        this.model = model;
        this.price = price;
    }


    public Shoe() { }

}

