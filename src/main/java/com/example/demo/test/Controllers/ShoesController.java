package com.example.demo.test.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.test.models.cartModels.CartClothesModel;
import com.example.demo.test.models.cartModels.CartShoeModel;
import com.example.demo.test.models.cartModels.cart_items.ClothesItem;
import com.example.demo.test.models.cartModels.cart_items.ShoeItem;
import com.example.demo.test.models.classModels.FileDB;
import com.example.demo.test.models.classModels.Sizes;
import com.example.demo.test.models.entities.Clothes;
import com.example.demo.test.models.entities.Favorite;
import com.example.demo.test.models.entities.Shoe;
import com.example.demo.test.models.entities.User;
import com.example.demo.test.models.enums.Brands;
import com.example.demo.test.models.enums.Colors;
import com.example.demo.test.models.enums.Conditions;
import com.example.demo.test.repositories.CartRepository;
import com.example.demo.test.repositories.ClothesRepository;
import com.example.demo.test.repositories.ShoeRepository;
import com.example.demo.test.repositories.authRepositories.UserRepository;
import com.example.demo.test.services.CartService;
import com.example.demo.test.services.ClothesService;
import com.example.demo.test.services.ShoeService;
import com.example.demo.test.services.UserService;


@Controller
@RequestMapping(value = {"{lang}/shoes", "/shoes"})
@PreAuthorize("hasRole('ROLE_USER')")
public class ShoesController {

    @Autowired
    private CartRepository _cartRepository;

    @Autowired
    private CartService _cartService;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private UserService _userService;

    //@Autowired private EmailService _emailService;

    @Autowired
    private ClothesRepository _clothesRepository;

    @Autowired
    private ClothesService _clothesService;

    @Autowired
    private ShoeRepository _shoeRepository;

    private ShoeService _shoeService;

    private final String mainControllerUrl = "http://localhost:8080/shoes";

    private final String mainBgControllerUrl = "http://localhost:8080/bg/shoes";

    @Autowired
    public ShoesController(ShoeService shoeService) {
        this._shoeService = shoeService;
    }

    public RedirectView redirectView(){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(mainControllerUrl);
        return redirectView;
    }

    @GetMapping("/{id}")
    public String getShoeById(@PathVariable String id, Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();
        Shoe shoe = this._shoeService.GetById(id);
        model.addAttribute("shoe", shoe);
        var relatedShoes = _shoeService.getAll()
                                       .stream()
                                       .filter(s -> {
                                            if (s.id.equals(id)) {
                                                return false;
                                            }
                                            return true;
                                        })
                                        .filter(s -> s.isAuctionOffer != true)
                                        .filter(s -> s.brand.equals(shoe.brand))
                                        .limit(5)
                                        .unordered()
                                        .collect(Collectors.toList());

        Optional<Favorite> favForUser = _shoeService.GetAllFavorites().stream().filter(fav -> fav.user.id.equals(_userService.getCurrentUser().id)).findAny();
        if (favForUser.isPresent()) {
            model.addAttribute("isAddedToFavorites", favForUser.get().shoes.stream().filter(cl -> cl.id.equals(shoe.id)).findAny().isPresent());
        }                            
        model.addAttribute("shoes", relatedShoes);
        model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        
       // _carService.setLanguage(request, model);

        _shoeService.removeAllFilters();
        
        return "user_pages/product_pages/shoes/product_page.html";
    }

    @PostMapping("/create")
    public RedirectView create(Brands brand, String model, String description, 
                               String releaseDate, Integer rating, String[] sizes, 
                               Conditions condition, Colors color, String colorSpecification, Integer price, Boolean isAuctionOffer, 
                               @RequestParam("mainImage") MultipartFile mainImage, 
                               @RequestParam("images") MultipartFile[] images){
        try {
            String imagesPath = System.getProperty("images.path");

            List<Sizes> sizesInStock = new ArrayList<>();
            if (sizes != null) {
                _shoeService.addSizesForShoe(sizes, sizesInStock);    
            }
            
            String newShoesDirectoryName = _shoeService.concatenate(brand.toString().toLowerCase(), "_", model.toLowerCase().replace(" ", "-"), "_", colorSpecification.toLowerCase().replace(" ", "-"));
            File newShoesDirectory = new File(imagesPath, newShoesDirectoryName);
            
            
            if (newShoesDirectory.mkdir()) {
                String mainImageName = StringUtils.cleanPath(mainImage.getOriginalFilename());
                //FileDB mainImageFile = new FileDB(mainImageName, mainImage.getContentType(), mainImage.getBytes());

                File mainImageDirectory = new File(newShoesDirectory, "main-image");
                String mainImageUrl = "";
                if (mainImageDirectory.mkdir()) {
                    mainImageUrl = newShoesDirectoryName + "/main-image/" + mainImageName;
                    this._shoeService.uploadImage(mainImage, mainImageName, mainImageDirectory);    
                }

                List<FileDB> sliderImageFiles = new ArrayList<>();     

                if (isAuctionOffer) {

                    //Upload multiple images for auction stocks
                    File manyImagesDirectory = new File(newShoesDirectory, "slider-images");
                    if (manyImagesDirectory.mkdir()) {
                        for (MultipartFile image : images) {
                            String imageName = image.getOriginalFilename();
                            String sliderImageUrl = newShoesDirectoryName + "/slider-images/" + imageName;
                            FileDB fileDB = new FileDB(imageName, image.getContentType(), sliderImageUrl);
                            sliderImageFiles.add(fileDB);                      
                            this._shoeService.uploadImage(image, imageName, manyImagesDirectory);    
                        }     
                    }
                }
                Shoe shoe = new Shoe(brand, model, description, releaseDate, sizesInStock, rating, condition, color, colorSpecification, price, isAuctionOffer, mainImageUrl, sliderImageFiles);
                _shoeService.create(shoe);
            }                
        } 
        catch (Exception e) {
            e.printStackTrace();
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView("http://localhost:8080/bg/admin");
        }
        return redirectView("http://localhost:8080/admin");
    }

    @GetMapping("/checkout")
    public String returnCheckoutForm(Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();

        User userEntity = _userRepository.findByEmail(request.getUserPrincipal().getName());
        if (_cartRepository.findAll().size() != 0) {
            var cartItemsForUser = _cartRepository.findAll()
                                                  .stream()
                                                  .filter(cart -> cart.user.email.equals(userEntity.email))
                                                  .findAny();
            if (cartItemsForUser.isPresent()) {
                model.addAttribute("totalPrice", _cartService.totalPriceSummary(cartItemsForUser.get()));     
                model.addAttribute("shoesItems", cartItemsForUser.get().shoesItems);
                model.addAttribute("clothesItems", cartItemsForUser.get().clothesItems);
            }
            else{
                model.addAttribute("totalPrice", 0);
                model.addAttribute("shoesItems", new ArrayList<ShoeItem>()); 
                model.addAttribute("clothesItems", new ArrayList<ClothesItem>()); 
            }     
        }
        else{
            model.addAttribute("totalPrice", 0);
            model.addAttribute("shoesItems", new ArrayList<ShoeItem>()); 
                model.addAttribute("clothesItems", new ArrayList<ClothesItem>());
        }
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian()); 
       // _carService.setLanguage(request, model);

        _shoeService.removeAllFilters();
        _clothesService.removeAllFilters();

        return "/user_pages/checkout-templates/checkout.html";
    }

    @GetMapping("/checkout/completed")
    public String completeOrder(Model model, String buyerFName, String buyerLName, String cardMonth, String cardYear, HttpServletRequest request){
        User userEntity = _userRepository.findByEmail(request.getUserPrincipal().getName());
        var cartProductForUser = _cartRepository.findAll()
                                                .stream()
                                                .filter(cartProduct -> cartProduct.user.email.equals(userEntity.email))
                                                .findAny();
        if (cartProductForUser.isPresent()) {
            model.addAttribute("shoesItems", cartProductForUser.get().shoesItems);
            model.addAttribute("clothesItems", cartProductForUser.get().clothesItems); 
            model.addAttribute("totalPrice", _cartService.totalPriceSummary(cartProductForUser.get()));
            _cartService.calculateOrder(model, userEntity.username, _cartService.totalPriceSummary(cartProductForUser.get()));
            HashMap<String, String> buyerModelAttributes = new HashMap<>();
            buyerModelAttributes.put("buyerFName", buyerFName);
            buyerModelAttributes.put("buyerLName", buyerLName);
            buyerModelAttributes.put("cardMonth", cardMonth);
            buyerModelAttributes.put("cardYear", cardYear);
            model.addAttribute("buyerData", buyerModelAttributes);
        }
        /*else {
            model.addAttribute("shoesItems", new ArrayList<ShoeItem>()); 
            model.addAttribute("clothesItems", new ArrayList<ClothesItem>());    
            model.addAttribute("totalPrice", 0);
            model.addAttribute("username", _userService.getCurrentUserName());
            model.addAttribute("isAdmin", _userService.isAdmin());
            model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
            //_carService.setLanguage(request, model);
            return "checkout-template/checkout.html";
        }*/
        
        //_emailService.sendEmail(userEntity.email, "purchase completed", "You completed your purchase successfully!");
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin()); 
        _cartRepository.delete(cartProductForUser.get());
        return "/user_pages/checkout-templates/purchase_completed.html";
    }

    @PostMapping("/{id}/addToCart")
    public RedirectView addProductToCart(@PathVariable String id, @RequestParam(name = "size") String sizeSelected, Model model, HttpServletRequest request){
        
        User userEntity = _userRepository.findByEmail(request.getUserPrincipal().getName());            
        var shoe = _shoeRepository.findById(id);
        if (shoe.isPresent()) {
            Shoe shoeEntity = shoe.get();
            CartShoeModel cartShoe = new CartShoeModel();
            cartShoe.id = id;
            cartShoe.brand = shoeEntity.brand;
            cartShoe.model = shoeEntity.model;
            cartShoe.condition = shoeEntity.condition;
            cartShoe.price = shoeEntity.price;
            cartShoe.size = sizeSelected;
            cartShoe.releaseDate = shoeEntity.releaseDate;
            cartShoe.colorSpecification = shoeEntity.colorSpecification;
            cartShoe.isAuctionOffer = shoeEntity.isAuctionOffer;
            cartShoe.mainImageUrl = shoeEntity.mainImageUrl;
            _cartService.addToCart(cartShoe, null, shoeEntity, null, userEntity);
        }
        else {
            Clothes cloth = _clothesRepository.findById(id).get();
            CartClothesModel cartCloth = new CartClothesModel();
            cartCloth.id = id;
            cartCloth.brand = cloth.brand;
            cartCloth.model = cloth.model;
            cartCloth.price = cloth.price;
            cartCloth.size = sizeSelected;
            cartCloth.releaseDate = cloth.releaseDate;
            cartCloth.colorSpecification = cloth.colorSpecification;
            cartCloth.imagePath = cloth.images.get(cloth.images.size() - 1).imagePath;
            _cartService.addToCart(null, cartCloth, null, cloth, userEntity);
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl + "/checkout");
        }

        return redirectView(mainControllerUrl + "/checkout");
    }

    @PostMapping("/addToFavorites/{id}")
    public RedirectView addToFavorites(@PathVariable String id, HttpServletRequest request){

        var shoe = _shoeRepository.findById(id);
        var user = request.getUserPrincipal().getName(); 
        if (shoe.isPresent()) {
            _shoeService.addToFavorites(null, shoe.get(), user);         
        }
        else {
            var cloth = _clothesRepository.findById(id).get();
            _shoeService.addToFavorites(cloth, null, user);
            return redirectView("http://localhost:8080/clothes/" + id);
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl + "/" + id);
        }
        return redirectView(mainControllerUrl + "/" + id);
    }

    @GetMapping("/favorites")
    public String getAllFavorites(Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();
        var currentUser = _userRepository.findByEmail(request.getUserPrincipal().getName());
        
        _shoeService.setModelFavoritesForUser(currentUser, model);
        
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());

        return "user_pages/favorites_list.html";
    }

    @PostMapping("/favorites/delete/{id}")
    public RedirectView deleteFavoriteDeal(@PathVariable String id, HttpServletRequest request) {

        this._shoeService.deleteFavoriteDealForUser(id, request);

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl + "/favorites");
        }
        return redirectView(mainControllerUrl + "/favorites");
    }

    @PostMapping("/checkout/{id}/delete")
    public RedirectView clearCartProduct(@RequestParam String id, HttpServletRequest request) {

        User userEntity = _userRepository.findByEmail(request.getUserPrincipal().getName());
        var cartProductForUser = _cartRepository.findAll()
                                                .stream()
                                                .filter(cartProduct -> cartProduct.user.email.equals(userEntity.email))
                                                .findFirst();
        if (cartProductForUser.isPresent()) {
            var shoeProduct = cartProductForUser.get().shoesItems.stream()
                                                        .filter(item -> item.product.id.equals(id))
                                                        .findFirst();
            
            if (shoeProduct.isPresent()) {
                cartProductForUser.get().shoesItems.remove(shoeProduct.get());
                _cartRepository.save(cartProductForUser.get());
            }
            else {
                var clothProduct = cartProductForUser.get().clothesItems.stream()
                                                        .filter(item -> item.product.id.equals(id))
                                                        .findFirst().get();
                cartProductForUser.get().clothesItems.remove(clothProduct);
                _cartRepository.save(cartProductForUser.get());                
            }
        }

        if (cartProductForUser.get().shoesItems.size() == 0 && cartProductForUser.get().clothesItems.size() == 0) {
            _cartRepository.delete(cartProductForUser.get());
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl + "/checkout");
        }

        return redirectView(mainControllerUrl + "/checkout");
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
