package com.example.demo.test.controllers;

import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.test.services.ClothesService;
import com.example.demo.test.services.ShoeService;
import com.example.demo.test.services.UserService;

@Controller
public class MainController {

    private ShoeService _shoeService;

    @Autowired
    private UserService _userService;

    @Autowired
    private ClothesService _clothesService;

    @Autowired
    public MainController(ShoeService shoeService) {
        this._shoeService = shoeService;
        _shoeService.pageFilters = new ArrayList<>();
        _shoeService.addFilters();
        _shoeService.currentFilteredList = shoeService.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
        _shoeService.filterIsRemoved = false;
    }

    @RequestMapping(value = {"{lang}/", "/"})
    public String viewHomePage(HttpServletRequest request, Model model) {
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();
        boolean isCurrentUserAuthenticated = true; //= SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        try {
            //String user = request.getUserPrincipal().getName();      
            model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
            model.addAttribute("username", _userService.getCurrentUser().username);
            model.addAttribute("isAdmin", _userService.isAdmin());       
        } catch (Exception e) {
            isCurrentUserAuthenticated = false;
        }

        if (_shoeService.getAll().size() < 9 && _clothesService.getAll().size() < 9) {
            model.addAttribute("enoughItems", false);
        }
        else {
            model.addAttribute("sliderShoes", _shoeService.divideShoesByParts());
            model.addAttribute("sliderClothes", _clothesService.divideClothesByParts());
        }
        //model.addAttribute("clothes", _clothesService.getAll().stream().limit(3).toList());
        //model.addAttribute("shoes", _shoeService.getAll().stream().filter(shoe -> shoe.isAuctionOffer != true).limit(3).toList());
        model.addAttribute("isSignedIn", isCurrentUserAuthenticated);
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());

        return "user_pages/index.html";
    }

    @RequestMapping(value = {"/shoes/get"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public String returnShoesMainPage(Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();

        var sliderShoes = _shoeService.getAll()
                                .stream()
                                .limit(6)
                                .collect(Collectors.toList());
        //var appProperty = System.getProperty("home.welcome");
        //model.addAttribute("appProperty", appProperty);
        model.addAttribute("sliderShoes", sliderShoes);
        model.addAttribute("shoesCount", sliderShoes.size());
        model.addAttribute("shoes", _shoeService.getAll().stream().filter(shoe -> shoe.isAuctionOffer != true).limit(8).toList());
        model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        //_carService.setLanguage(request, model);

        _shoeService.removeAllFilters();
        _clothesService.removeAllFilters();
        
        return "user_pages/user_main_page.html";
    }

    
}
