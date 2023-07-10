package com.example.demo.test.Services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.test.Models.cartModels.CartClothesModel;
import com.example.demo.test.Models.cartModels.CartShoeModel;
import com.example.demo.test.Models.cartModels.cart_items.ClothesItem;
import com.example.demo.test.Models.cartModels.cart_items.ShoeItem;
import com.example.demo.test.Models.classModels.Order;
import com.example.demo.test.Models.entities.CartProduct;
import com.example.demo.test.Models.entities.Clothes;
import com.example.demo.test.Models.entities.DailyTurnOver;
import com.example.demo.test.Models.entities.Shoe;
import com.example.demo.test.Models.entities.User;
import com.example.demo.test.Repositories.CartRepository;
import com.example.demo.test.Repositories.TurnOverRepository;

@Service
public class CartService {

    private CartRepository _cartRepository;

    @Autowired TurnOverRepository _turnOverRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this._cartRepository = cartRepository;
    }

    public void addToCart(CartShoeModel shoeProduct, CartClothesModel clothProduct, Shoe shoe, Clothes cloth, User user){
        boolean isEnough = false;
        boolean isEnd = false;
        boolean noUserFound = false;
        int countOfIterations = 0;
        if (!_cartRepository.findAll().isEmpty()) {
            //int countOfEqualCartProducts = 0;
            //CartProduct cartProduct = new CartProduct();
            for (CartProduct entity : _cartRepository.findAll()) {
                if (entity.user.email.equals(user.email)) {
                    noUserFound = false; 
                    if (shoeProduct != null) {
                        if (entity.shoesItems.size() == 0) {
                            ShoeItem itemForCart = new ShoeItem();
                            itemForCart.product = shoeProduct;
                            itemForCart.quantity++;
                            entity.addShoeItem(itemForCart);
                            _cartRepository.save(entity);
                        }
                        else {
                            for (ShoeItem item : entity.shoesItems) {
                                if (item.product.id.equals(shoeProduct.id) && item.product.size.equals(shoeProduct.size)) {
                                    item.quantity++; 
                                    _cartRepository.save(entity); 
                                    isEnd = true;
                                }
                                countOfIterations++;
                                if (countOfIterations == entity.shoesItems.size() && isEnd == false) {
                                    isEnough = true;
                                }
                
                                if(isEnough){
                                    ShoeItem itemForCart = new ShoeItem();
                                    itemForCart.product = shoeProduct;
                                    itemForCart.quantity++;
                                    entity.addShoeItem(itemForCart);
                                    _cartRepository.save(entity);
                                    isEnd = true;
                                }
                
                                if(isEnd){
                                    /*for (Sizes size : shoe.sizes) {
                                        if (size.size.equals(shoeProduct.size) && size.isInStock) {
                                            size.quantity--;
                                            if (size.quantity == 0) {
                                                size.isInStock = false;
                                            }
                                            _shoeRepository.save(shoe);
                                            int r = 0;
                                        }
                                    }*/
                                    break;
                                }
                            }
                        }
                    }
                    else if (clothProduct != null) {
                        if (entity.clothesItems.size() == 0) {
                            ClothesItem itemForCart = new ClothesItem();
                            itemForCart.product = clothProduct;
                            itemForCart.quantity++;
                            entity.addClothItem(itemForCart);
                            _cartRepository.save(entity);
                        }
                        else {
                            for (ClothesItem item : entity.clothesItems) {
                                if (item.product.id.equals(clothProduct.id) && item.product.size.equals(clothProduct.size)) {
                                    item.quantity++; 
                                    _cartRepository.save(entity); 
                                    isEnd = true;
                                }
                                countOfIterations++;
                                if (countOfIterations == entity.clothesItems.size() && isEnd == false) {
                                    isEnough = true;
                                }
                
                                if(isEnough){
                                    //CartProduct cartProduct = new CartProduct();
                                    ClothesItem itemForCart = new ClothesItem();
                                    itemForCart.product = clothProduct;
                                    itemForCart.quantity++;
                                    entity.addClothItem(itemForCart);
                                    _cartRepository.save(entity);
                                    isEnd = true;
                                }
                
                                if(isEnd){
                                    break;
                                }
                            }  
                        }
                    }                
                }
                else{
                    noUserFound = true;
                    continue;
                }
            }            
        }
        else {
            CartProduct cartProduct = new CartProduct();
            if (shoeProduct != null) {
                ShoeItem itemForCart = new ShoeItem();
                itemForCart.product = shoeProduct;
                itemForCart.quantity++;
                cartProduct.addShoeItem(itemForCart); 
            }
            else if (clothProduct != null){
                ClothesItem itemForCart = new ClothesItem();
                itemForCart.product = clothProduct;
                itemForCart.quantity++;
                cartProduct.addClothItem(itemForCart);
            }
            cartProduct.addUser(user);
            //shoe.sizes.get(shoe.sizes.indexOf(shoeProduct.size)).quantity--;
            //_shoeRepository.save(shoe);
            _cartRepository.save(cartProduct);
        }

        if (noUserFound) {
            CartProduct cartProduct = new CartProduct();
            if (shoeProduct != null) {
                ShoeItem itemForCart = new ShoeItem();
                itemForCart.product = shoeProduct;
                itemForCart.quantity++;
                cartProduct.addShoeItem(itemForCart); 
            }
            else if (clothProduct != null){
                ClothesItem itemForCart = new ClothesItem();
                itemForCart.product = clothProduct;
                itemForCart.quantity++;
                cartProduct.addClothItem(itemForCart);
            }
            cartProduct.addUser(user);
            _cartRepository.save(cartProduct);
        }
    }

    public Integer totalPriceSummary(CartProduct cartProduct){
        int priceSummary = 0;
        for (ShoeItem item : cartProduct.shoesItems) {
            priceSummary += item.quantity * item.product.price;
        }
        for (ClothesItem item : cartProduct.clothesItems) {
            priceSummary += item.quantity * item.product.price;
        }
        return priceSummary;
    }

    public void calculateOrder(Model model, String username, Integer totalPriceSummary) {
        //add a functionality for adding an dailyTurnOver for each day according to date.
        //Then, add the order to the daily orders in the turnover for today
        //if the day changes, add a new turnover
        //keep adding each order to the daily turnover until the day changes
        //put an order number for each new order (orderNumber, username, summaryFromOrder)
        boolean isEnough = false;
        boolean isEnd = false;
        int countOfIterations = 0;
        if (_turnOverRepository.findAll().isEmpty()) {
            DailyTurnOver turnOver = new DailyTurnOver();
            turnOver.date = new Date();
            Order order = new Order(1, username, totalPriceSummary);
            turnOver.orders.add(order);
            _turnOverRepository.save(turnOver);
        }
        else {
            for (DailyTurnOver existingTurnOver : _turnOverRepository.findAll()) {
                if (isSameDay(existingTurnOver.date, new Date())) {
                    Order order = new Order(existingTurnOver.orders.size() + 1, username, totalPriceSummary);
                    existingTurnOver.orders.add(order);
                    _turnOverRepository.save(existingTurnOver);
                    isEnd = true;
                }
                else {
                    countOfIterations++;
                    if (countOfIterations == _turnOverRepository.findAll().size() && isEnd == false) {
                        isEnough = true;
                    }
                }

                if (isEnd) {
                    break;
                }
            }

            if (isEnough) {
                DailyTurnOver newTurnOver = new DailyTurnOver();
                newTurnOver.date = new Date();
                Order dailyOrder = new Order(1, username, totalPriceSummary);
                newTurnOver.orders.add(dailyOrder);
                _turnOverRepository.save(newTurnOver);
            }
            
        }
    }

    public static boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    public Integer sumTotalTurnOverForToday(){
        int summaryForTheDay = 0;
        Date currentDate = new Date();
        if (!_turnOverRepository.findAll().isEmpty()) {
            var dailyTurnOver = _turnOverRepository.findAll()
                                                   .stream()
                                                   .filter(turnOver -> {
                                                        if (isSameDay(turnOver.date, currentDate)) {
                                                            return true;
                                                        }
                                                        else {
                                                            return false;
                                                        }
                                                   })
                                                   .findAny();
            if (dailyTurnOver.isPresent()) {
                for (Order order : dailyTurnOver.get().orders) {
                    summaryForTheDay += order.priceSummaryFromOrder;
                }
            }
        }
        return summaryForTheDay;
    }

    public List<Order> getAllOrdersForToday(){
        Date currentDate = new Date();
        if (!_turnOverRepository.findAll().isEmpty()) {
            var dailyTurnOver = _turnOverRepository.findAll()
                                                   .stream()
                                                   .filter(turnOver -> {
                                                        if (isSameDay(turnOver.date, currentDate)) {
                                                            return true;
                                                        }
                                                        else {
                                                            return false;
                                                        }
                                                   })
                                                   .findAny();
            if (dailyTurnOver.isPresent()) {
                return dailyTurnOver.get().orders;
            }
        }
        return new ArrayList<Order>();
    }
}
