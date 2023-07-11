package com.example.demo.test.models.cartModels.cart_items;

import com.example.demo.test.models.cartModels.CartClothesModel;

public class ClothesItem {
    public CartClothesModel product;

    public Integer quantity;

    public ClothesItem() {
        this.quantity = 0;
    }

    public ClothesItem(CartClothesModel product) {
        this.product = product;
        this.quantity = 0;
    }
}
