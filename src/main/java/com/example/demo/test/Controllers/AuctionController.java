package com.example.demo.test.controllers;

import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.test.models.entities.Comment;
import com.example.demo.test.models.entities.Shoe;
import com.example.demo.test.models.entities.User;
import com.example.demo.test.repositories.CommentRepository;
import com.example.demo.test.repositories.authRepositories.UserRepository;
import com.example.demo.test.services.ClothesService;
import com.example.demo.test.services.ShoeService;
import com.example.demo.test.services.UserService;


@Controller
@RequestMapping(value = {"/auction", "/bg/auction"})
public class AuctionController {

    private ShoeService _shoeService;

    @Autowired
    private UserService _userService;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private ClothesService _clothesService;

    @Autowired
    private CommentRepository _commentRepository;

    private final String mainControllerUrl = "http://localhost:8080/auction";

    private final String mainBgControllerUrl = "http://localhost:8080/bg/auction";

    @Autowired
    public AuctionController(ShoeService shoeService) {
        this._shoeService = shoeService;
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();
        try {
            var auctionShoes = _shoeService.getAll()
                                           .stream()
                                           .filter(shoe -> shoe.isAuctionOffer == true)
                                           .collect(Collectors.toList());
            model.addAttribute("auctionShoes", auctionShoes);
            model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
            model.addAttribute("username", _userService.getCurrentUser().username);
            model.addAttribute("isAdmin", _userService.isAdmin());
            model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());       
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error.html";
        }

        _clothesService.removeAllFilters();
        _shoeService.removeAllFilters();

        return "auction_page.html";
    }

    @GetMapping("/{id}")
    public String getShoeById(@PathVariable String id, Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();
        try {
            Shoe shoe = this._shoeService.GetById(id);
            model.addAttribute("shoe", shoe);
            model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
            model.addAttribute("username", _userService.getCurrentUser().username);
            model.addAttribute("isAdmin", _userService.isAdmin());
            model.addAttribute("comments", _shoeService.getAllCommentsForShoe(id));
            model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
            model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());   
        } catch (Exception e) {
            return "error.html";
        }
        return "auction_product_page.html";
    }

    @PostMapping("/{shoeId}/comment")
    public RedirectView addCommentForAuctionShoe(@PathVariable String shoeId, String content){
        Date date = new Date();
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userEmail.isEmpty()) {
            User user = _userRepository.findByEmail(userEmail);
            Comment comment = new Comment(shoeId, user.username, content, date); 
            _commentRepository.save(comment);
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl + "/" + shoeId);
        }
        return redirectView(mainControllerUrl + "/" + shoeId);
    }

    @PostMapping("/{shoeId}/comment/delete/{commentId}")
    public RedirectView deleteCommentForAuctionShoe(@PathVariable String shoeId, @PathVariable String commentId){
        var comment = _commentRepository.findById(commentId);
        if (comment.isPresent()) {
            if (comment.get().shoeId.equals(shoeId)) {
                _commentRepository.delete(comment.get());
            }
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl + "/" + shoeId);
        }
        return redirectView(mainControllerUrl + "/" + shoeId);
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }

}
