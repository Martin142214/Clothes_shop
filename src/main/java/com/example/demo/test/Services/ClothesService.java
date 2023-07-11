package com.example.demo.test.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.test.models.classModels.Filter;
import com.example.demo.test.models.classModels.Sizes;
import com.example.demo.test.models.classModels.SliderClothes;
import com.example.demo.test.models.entities.Clothes;
import com.example.demo.test.repositories.ClothesRepository;

@Service
public class ClothesService {

    public List<Filter> pageFilters;

    public List<Clothes> currentFilteredList;
/* 
    //brand filter
    private Filter brandFilter;
    
    //size category (men, women) filter
    private Filter genderFilter;
    
    //clothes size (XS, L, XXL) filter
    private Filter sizeFilter;
    
    //shoe price filter
    private Filter priceFilter;*/
    
    private boolean brandFilterPlaced;
    
    private boolean genderFilterPlaced;
    
    private boolean sizeFilterPlaced;
    
    private boolean priceFilterPlaced;

    //removed filter tracker
    public boolean filterIsRemoved;

    @Autowired
    private ClothesRepository _clothesRepository;
    
    public ClothesService() {
        
    }

    public List<Clothes> getAll() {
        return this._clothesRepository.findAll();
    }

    public void addFilters(){
        pageFilters.clear();
        pageFilters.add(new Filter("brand", ""));
        pageFilters.add(new Filter("gender", ""));
        pageFilters.add(new Filter("size", ""));
        pageFilters.add(new Filter("price", ""));
    }

    public void placeBrandFilter(String brand){
        if (brand != null) {
            if (brandFilterPlaced) {
                removeFilter(0, pageFilters);
                currentFilteredList = _clothesRepository.findAll();
            }
            pageFilters.get(0).value = brand;
            applyCurrentFilters(pageFilters);
            brandFilterPlaced = true;
        }
    }

    public void placeGenderFilter(String gender){
        if (gender != null) {
            if (genderFilterPlaced) {
                removeFilter(1, pageFilters);
                currentFilteredList = _clothesRepository.findAll();
            }
            pageFilters.get(1).value = gender;
            applyCurrentFilters(pageFilters);
            genderFilterPlaced = true;
        } 
    }

    public void placeSizeFilter(String size){
        if (size != null) {
            if (sizeFilterPlaced) {
                removeFilter(2, pageFilters);
                currentFilteredList = _clothesRepository.findAll();
            }
            pageFilters.get(2).value = size;
            applyCurrentFilters(pageFilters);
            sizeFilterPlaced = true;
        }
    }

    public void placePriceFilter(String price){
        if (price != null) {
            if (priceFilterPlaced) {
                removeFilter(3, pageFilters);
                currentFilteredList = _clothesRepository.findAll();
            }
            pageFilters.get(3).value = price;
            applyCurrentFilters(pageFilters);
            priceFilterPlaced = true;
        }
    }


    public void removeFilter(int index, List<Filter> filters){
        filters.get(index).value = "";
        filterIsRemoved = true;
    }

    public void removeAllFilters(){
        for (Filter filter : pageFilters) {
            filter.value = "";
        }
        filterIsRemoved = true;

        if (filterIsRemoved) {
            currentFilteredList = _clothesRepository.findAll();
            applyCurrentFilters(pageFilters);
            filterIsRemoved = false;
        }
    }


    public void detachFilter(String filter){
        for (Filter filt : pageFilters) {
            if (filt.type.toLowerCase().equals(filter.toLowerCase())) {
                filt.value = "";
                filterIsRemoved = true;
            }
        }

        if (filterIsRemoved) {
            currentFilteredList = _clothesRepository.findAll();
            applyCurrentFilters(pageFilters);
            filterIsRemoved = false;
        }
    }

    public void applyCurrentFilters(List<Filter> filters){
        for (Filter filter : filters) {
            if (filter.type == "brand" && filter.value != "") {
                currentFilteredList = currentFilteredList
                .stream()
                .filter(cloth -> cloth.brand.toString().toLowerCase().equals(filter.value.toLowerCase()))
                .collect(Collectors.toList());
                //this.brandFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
            if (filter.type == "gender" && !filter.value.equals("")) {
                currentFilteredList = currentFilteredList
                .stream()
                .filter(cloth -> cloth.gender.toString().toLowerCase().equals(filter.value.toLowerCase()))
                .collect(Collectors.toList());
                //this.genderFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
            if (filter.type == "size" && filter.value != "") {
                currentFilteredList = currentFilteredList
                .stream()
                .filter(cloth -> {
                    Iterator<Sizes> it = cloth.sizes.iterator();
                    while (it.hasNext()) {          
                        if (it.next().size.toLowerCase().equals(filter.value.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
                //this.sizeFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
            if (filter.type == "price" && filter.value != "") {
                if (filter.value.contains("-")) {
                    String[] priceRangeValues = filter.value.split("-");
                    int lowerPrice = Integer.parseInt(priceRangeValues[0]);
                    int upperPrice = Integer.parseInt(priceRangeValues[1]);
                    currentFilteredList = currentFilteredList
                    .stream()
                    .filter(cloth -> cloth.price >= lowerPrice && cloth.price <= upperPrice)
                    .collect(Collectors.toList());
                }
                else{
                    int price = Integer.parseInt(filter.value);
                    currentFilteredList = currentFilteredList
                    .stream()
                    .filter(cloth -> cloth.price >= price)
                    .collect(Collectors.toList());
                }
                //this.priceFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
        }
    }

    public Collection<SliderClothes> divideClothesByParts() {
        var clothes = this.getAll();
        Collection<SliderClothes> sliderClothes = new ArrayList<>();

        if (clothes.size() >= 9) {
            SliderClothes slideClothes = new SliderClothes();
            for (int i = 1; i < 10; i++) {
    
                slideClothes.clothesParts.add(clothes.get(i-1));
    
                if (i % 3 == 0) {
                    sliderClothes.add(slideClothes);
                    slideClothes = new SliderClothes();
                }
            }  
        }
        return sliderClothes;
    } 
}
