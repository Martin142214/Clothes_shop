package com.example.demo.test.Models.cartModels.cart_items;


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
