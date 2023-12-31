package com.example.demo.test.models.entities;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.test.models.classModels.FileDB;
import com.example.demo.test.models.classModels.Sizes;
import com.example.demo.test.models.enums.ClothesBrands;
import com.example.demo.test.models.enums.Colors;

@Document("Clothes")
public class Clothes implements Serializable {
    
    @Id
    public String id;
    public ClothesBrands brand;
    public String model;
    public String description;
    public String releaseDate;
    public List<Sizes> sizes;
    public Colors color;
    public String colorSpecification;
    public String gender;
    public Integer price;
    public List<FileDB> images;

    public Clothes(ClothesBrands brand, String model, String description, String releaseDate, List<Sizes> sizes,
                    Colors color, String colorSpecificatiion, String gender, Integer price, List<FileDB> images) {
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.releaseDate = releaseDate;
        this.sizes = sizes;
        this.color = color;
        this.colorSpecification = colorSpecificatiion;
        this.gender = gender;
        this.price = price;
        this.images = images;
    }

    public Clothes(ClothesBrands brand, String model, Integer price) {
        this.brand = brand;
        this.model = model;
        this.price = price;
    }

    public Clothes() { }

}
