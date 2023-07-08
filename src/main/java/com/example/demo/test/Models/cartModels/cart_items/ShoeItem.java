package com.example.demo.test.Models.cartModels.cart_items;

import com.example.demo.test.Models.cartModels.CartShoeModel;

public class ShoeItem {
    public CartShoeModel product;

    public Integer quantity;

    public ShoeItem() {
        this.quantity = 0;
    }

    public ShoeItem(CartShoeModel product) {
        this.product = product;
        this.quantity = 0;
    }
}
