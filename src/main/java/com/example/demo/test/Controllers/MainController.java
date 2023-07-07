package com.example.demoTEST2.controllers;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demoTEST2.services.ClothesService;
import com.example.demoTEST2.services.ShoeService;
import com.example.demoTEST2.services.UserService;

@Controller
@RequestMapping(value = {"{lang}/shoes/get", "/shoes/get"})
@PreAuthorize("hasRole('ROLE_USER')")
public class MainController {

    private ShoeService _shoeService;

    @Autowired
    private UserService _userService;

    @Autowired
    private ClothesService _clothesService;

    @Autowired
    public MainController(ShoeService shoeService) {
        this._shoeService = shoeService;
    }

    @GetMapping(value = {"/bg", ""})
    public String viewHomePage(HttpRequest request, Model model) {
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

        model.addAttribute("sliderShoes", _shoeService.divideShoesByParts());
        model.addAttribute("sliderClothes", _clothesService.divideClothesByParts());
        //model.addAttribute("clothes", _clothesService.getAll().stream().limit(3).toList());
        //model.addAttribute("shoes", _shoeService.getAll().stream().filter(shoe -> shoe.isAuctionOffer != true).limit(3).toList());
        model.addAttribute("isSignedIn", isCurrentUserAuthenticated);
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());

        return "index.html";
    }

    
}
