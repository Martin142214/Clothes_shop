package com.example.demo.test.models.enums;

import java.util.Random;

public enum ClothesBrands {
    Adidas,
    Jordan,
    Vans,
    Nike,
    Puma,
    Reebok,
    Gucci,
    Supreme;

    private static final Random PRNG = new Random();

    public static ClothesBrands randomBrand()  {
        ClothesBrands[] brands = values();
        return brands[PRNG.nextInt(brands.length)];
    }
}
