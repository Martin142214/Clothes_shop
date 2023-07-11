package com.example.demo.test.models.enums;

import java.util.Random;

public enum Brands {
    Adidas,
    Jordan,
    Vans,
    Nike,
    Puma,
    Reebok,
    Yeezy;

    private static final Random PRNG = new Random();

    public static Brands randomBrand()  {
        Brands[] brands = values();
        return brands[PRNG.nextInt(brands.length)];
    }
}
