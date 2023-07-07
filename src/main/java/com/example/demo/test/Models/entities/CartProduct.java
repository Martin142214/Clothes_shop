package com.example.demo.test.Models.entities;


import java.util.ArrayList;
import java.util.Collection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.test.Models.entities.cart_items.ClothesItem;
import com.example.demo.test.Models.entities.cart_items.ShoeItem;


@Document("Cart_Products")
public class CartProduct {
    
    @Id
    public String id;

    public Collection<ShoeItem> shoesItems;

    public Collection<ClothesItem> clothesItems;

    public User user;

    public CartProduct() {
        shoesItems = new ArrayList<ShoeItem>();
        clothesItems = new ArrayList<ClothesItem>();
    }

    public CartProduct(Collection<ShoeItem> shoesItems, Collection<ClothesItem> clothesItems, User user) {
        this.shoesItems = shoesItems;
        this.clothesItems = clothesItems;
        this.user = user;
    }

    public void addShoeItem(ShoeItem item){
        this.shoesItems.add(item);
    }

    public void addClothItem(ClothesItem item){
        this.clothesItems.add(item);
    }

    public void addUser(User user){
        this.user = user;
    }
}
