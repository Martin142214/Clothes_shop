package com.example.demo.test.Services;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.test.FileUploadUtils;
import com.example.demo.test.Models.classModels.Filter;
import com.example.demo.test.Models.classModels.Sizes;
import com.example.demo.test.Models.classModels.SliderShoes;
import com.example.demo.test.Models.entities.Clothes;
import com.example.demo.test.Models.entities.Comment;
import com.example.demo.test.Models.entities.Favorite;
import com.example.demo.test.Models.entities.Shoe;
import com.example.demo.test.Models.entities.User;
import com.example.demo.test.Repositories.CommentRepository;
import com.example.demo.test.Repositories.FavoritesRepository;
import com.example.demo.test.Repositories.ShoeRepository;
import com.example.demo.test.Repositories.authRepositories.UserRepository;

@Service
public class ShoeService {
    
    private ShoeRepository _shoeRepository;

    private FavoritesRepository _favoritesRepository;

    @Autowired
    private CommentRepository _commentRepository;

    @Autowired
    private UserRepository _userRepository;

    public List<Filter> pageFilters;

    public List<Shoe> currentFilteredList;

    /* 
    //brand filter
    private Filter brandFilter;
   
    //size category (men, women) filter
    private Filter colorFilter;
      
    //shoe size (5.5, 6.5) filter
    private Filter shoeSizeFilter;
    
    //shoe price filter
    private Filter shoePriceFilter; */

    private boolean brandFilterPlaced;
    
    private boolean colorFilterPlaced;
    
    private boolean shoeSizeFilterPlaced;
    
    private boolean shoePriceFilterPlaced;

    //removed filter tracker
    public boolean filterIsRemoved;

    public String currentControllerUrl = "/";

    @Autowired
    public ShoeService(ShoeRepository _shoeRepository, FavoritesRepository favoritesRepository) {
        this._shoeRepository = _shoeRepository;
        this._favoritesRepository = favoritesRepository;
    }

    public boolean isLanguageBulgarian(){
        if (!currentControllerUrl.equals("/")) {
            String[] uriParts = currentControllerUrl.split("/");
            if (uriParts[1].equalsIgnoreCase("bg")) {
                return true;
            }
        }
        return false;
    }


    public List<Shoe> getAll(){
        List<Shoe> shoes = this._shoeRepository.findAll();
        return shoes;
    }

    public void create(Shoe shoe){
        this._shoeRepository.save(shoe);
    }


    public Shoe GetById(String id){
        Optional<Shoe> entity = this._shoeRepository.findById(id);
        if (entity.isPresent()) {
            return entity.get();
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shoe with id: " + id + " is not found");
        }
    }


    public void edit(Shoe shoe) {
        this._shoeRepository.save(shoe);
    }

    public void Delete(String id){
        this._shoeRepository.deleteById(id);
    }

    public void addFilters(){
        pageFilters.clear();
        pageFilters.add(new Filter("brand", ""));
        pageFilters.add(new Filter("color", ""));
        pageFilters.add(new Filter("size", ""));
        pageFilters.add(new Filter("price", ""));
    }
    public void placeBrandFilter(String brand){
        if (brand != null) {
            if (brandFilterPlaced) {
                removeFilter(0, pageFilters);
                currentFilteredList = this.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
            }
            pageFilters.get(0).value = brand;
            applyCurrentFilters(pageFilters);
            brandFilterPlaced = true;
            /*currentFilterList = currentFilterList
            .stream()
            .filter(deal -> deal.car.brand.toLowerCase().equals(brand.toLowerCase()))
            .collect(Collectors.toList());
            brandFilter = new Filter("brand", brand.toLowerCase());
            brandFilterPlaced = true;
            pageFilters.add(brandFilter);*/ 
        }
    }

    public void placeColorFilter(String color){
        if (color != null) {
            if (colorFilterPlaced) {
                removeFilter(1, pageFilters);
                currentFilteredList = this.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
            }
            pageFilters.get(1).value = color;
            applyCurrentFilters(pageFilters);
            colorFilterPlaced = true;
            /*
            currentFilterList = currentFilterList
            .stream()
            .filter(deal -> deal.car.model.toLowerCase().equals(category.toLowerCase()))
            .collect(Collectors.toList());
            sizeCategoryFilter = new Filter("category", category.toLowerCase()); */
        } 
    }

    public void placeSizeFilter(String size){
        if (size != null) {
            if (shoeSizeFilterPlaced) {
                removeFilter(2, pageFilters);
                currentFilteredList = this.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
            }
            pageFilters.get(2).value = size;
            applyCurrentFilters(pageFilters);
            shoeSizeFilterPlaced = true;
        }
    }

    public void placePriceFilter(String price){
        if (price != null) {
            if (shoePriceFilterPlaced) {
                removeFilter(3, pageFilters);
                currentFilteredList = this.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
            }
            pageFilters.get(3).value = price;
            applyCurrentFilters(pageFilters);
            shoePriceFilterPlaced = true;
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
            currentFilteredList = this.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
            applyCurrentFilters(pageFilters);
            filterIsRemoved = false;
        }
    }

    public void applyCurrentFilters(List<Filter> filters){
        for (Filter filter : filters) {
            if (filter.type == "brand" && filter.value != "") {
                currentFilteredList = currentFilteredList
                .stream()
                .filter(shoe -> shoe.brand.toString().toLowerCase().equals(filter.value.toLowerCase()))
                .collect(Collectors.toList());
                //brandFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
            if (filter.type == "color" && !filter.value.equals("")) {
                currentFilteredList = currentFilteredList
                .stream()
                .filter(shoe -> shoe.color.toString().toLowerCase().equals(filter.value.toLowerCase()))
                .collect(Collectors.toList());
                //colorFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
            if (filter.type == "size" && filter.value != "") {
                currentFilteredList = currentFilteredList
                .stream()
                .filter(shoe -> {
                    //BigDecimal.valueOf(Double.valueOf(shoe.sizes.iterator().next().size)).equals(size)
                    Iterator<Sizes> it = shoe.sizes.iterator();
                    while (it.hasNext()) {          
                        if (it.next().size.equals(filter.value)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
                //shoeSizeFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
            if (filter.type == "price" && filter.value != "") {
                if (filter.value.contains("-")) {
                    String[] priceRangeValues = filter.value.split("-");
                    int lowerPrice = Integer.parseInt(priceRangeValues[0]);
                    int upperPrice = Integer.parseInt(priceRangeValues[1]);
                    currentFilteredList = currentFilteredList
                    .stream()
                    .filter(shoe -> shoe.price >= lowerPrice && shoe.price <= upperPrice)
                    .collect(Collectors.toList());
                }
                else{
                    int price = Integer.parseInt(filter.value);
                    currentFilteredList = currentFilteredList
                    .stream()
                    .filter(shoe -> shoe.price >= price)
                    .collect(Collectors.toList());
                }
                //shoePriceFilter = new Filter(filter.type, filter.value.toLowerCase());
            }
        }
    }

    public void addSizesForShoe(String[] size, List<Sizes> sizesInStock){
        for (String s : size) {
            sizesInStock.add(new Sizes(s, 10, true));
        }
    }


    public void uploadImage(MultipartFile image, String imageName, File newDirectory) {
            try {
                FileUploadUtils.saveFile(newDirectory.getAbsolutePath(), imageName, image);
            } catch (IOException e) {
                e.printStackTrace();
            }

        //String uploadDir = "E:/Java IntelliJ projects/demo-TEST-2/src/main/resources/static/images/";
        //Path rootPath = FileSystems.getDefault().getPath("").toAbsolutePath();
        //Path filePath = Paths.get(rootPath.toString(), "src", "main", "resources", "static", "images");
    }

    public String concatenate(String... s)
    {   
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            sb = sb.append(s[i]);
        }
 
        return sb.toString();
    }


    public void addToFavorites(Clothes cloth, Shoe shoe, String user){
        int countOfEqualFavoriteDeals = 0;
        boolean haveFavoritesForUser = false;
        User userEntity = _userRepository.findByEmail(user);
        int userItems = 0;
        if (shoe != null) {
            for (Favorite entity : this.GetAllFavorites()) {
                if (entity.user.email.equals(userEntity.email)) {
                    haveFavoritesForUser = true;
                    for (Shoe objShoe : entity.shoes) {
                        if (objShoe.id.equals(shoe.id)) {
                            countOfEqualFavoriteDeals++;               
                        }
                        userItems++;
                    }
                }
                
            }
            if(countOfEqualFavoriteDeals == 0 && userItems == 0 && !haveFavoritesForUser){
                Favorite favorite = new Favorite();
                favorite.shoes = new ArrayList<>();
                favorite.clothes = new ArrayList<>();
                favorite.shoes.add(shoe);
                favorite.user = userEntity;
                this._favoritesRepository.save(favorite);
            }
            else if (countOfEqualFavoriteDeals == 0) {
                var userFavorites = this.GetAllFavorites()
                                        .stream()
                                        .filter(favorite -> favorite.user.email.equals(userEntity.email))
                                        .findAny();
                if (userFavorites.isPresent()) {
                    userFavorites.get().shoes.add(shoe);
                    this._favoritesRepository.save(userFavorites.get());     
                }
            }  
        }
        else if (cloth != null){
            for (Favorite entity : this.GetAllFavorites()) {
                if (entity.user.email.equals(userEntity.email)) {
                    haveFavoritesForUser = true;
                    for (Clothes objCloth : entity.clothes) {
                        if (objCloth.id.equals(cloth.id)) {
                            countOfEqualFavoriteDeals++;               
                        }
                        userItems++;
                    }
                }
                
            }
            if(countOfEqualFavoriteDeals == 0 && userItems == 0 && !haveFavoritesForUser){
                Favorite favorite = new Favorite();
                favorite.shoes = new ArrayList<>();
                favorite.clothes = new ArrayList<>();
                favorite.clothes.add(cloth);
                favorite.user = userEntity;
                this._favoritesRepository.save(favorite);
            }
            else if (countOfEqualFavoriteDeals == 0) {
                var userFavorites = this.GetAllFavorites()
                                        .stream()
                                        .filter(favorite -> favorite.user.email.equals(userEntity.email))
                                        .findAny();
                if (userFavorites.isPresent()) {
                    userFavorites.get().clothes.add(cloth);
                    this._favoritesRepository.save(userFavorites.get());     
                }
            }  
        }
    }

    public void setModelFavoritesForUser(User currentUser, Model model){
        var userFavorites = this.GetAllFavorites()
                                    .stream()
                                    .filter(favorite -> favorite.user.email.equals(currentUser.email))
                                    .findAny();

        if (userFavorites.isPresent()) {
            var countOfFavorites = Integer.sum(userFavorites.get().shoes.size(), userFavorites.get().clothes.size());
            model.addAttribute("shoesFavorites", userFavorites.get().shoes);
            model.addAttribute("clothesFavorites", userFavorites.get().clothes);       
            model.addAttribute("countOfFavorites", countOfFavorites);
        }
        else{
            model.addAttribute("shoesFavorites", new ArrayList<Shoe>());
            model.addAttribute("clothesFavorites", new ArrayList<Clothes>());
            model.addAttribute("countOfFavorites", 0);
            model.addAttribute("hasNoFavorites", true);
        }
    }

    /*public static <T> List<T> concatWithCollection(List<T> array1, List<T> array2) {
        List<T> resultList = new ArrayList<>(array1.size() + array2.size());
        Collections.addAll(resultList, array1);
        Collections.addAll(resultList, array2);
    }*/

    public List<Favorite> GetAllFavorites(){
        List<Favorite> favorites = this._favoritesRepository.findAll();
        return favorites;
    }

    public Integer getCountOfFavorites(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = _userRepository.findByEmail(user.getName());
        var userFavorites = this.GetAllFavorites()
                                    .stream()
                                    .filter(favorite -> favorite.user.email.equals(currentUser.email))
                                    .findAny();
        if (userFavorites.isPresent()) {
            return Integer.sum(userFavorites.get().shoes.size(), userFavorites.get().clothes.size());     
        }
        else {
            return 0;
        }
    }

    public List<Comment> getAllCommentsForShoe(String shoeId) {
        if (!_commentRepository.findAll().isEmpty()) {
            List<Comment> comments = _commentRepository.findAll()
                                                       .stream()
                                                       .filter(comment -> comment.shoeId.equals(shoeId))
                                                       .toList();
            if (comments.isEmpty()) {
                return new ArrayList<Comment>();
            }
            else {
                return comments;                                           
            }
        }
        else {
            return new ArrayList<Comment>();
        }
    }

    //TODO to delete
    /*public void addToCart(CartShoeModel shoeProduct, Shoe shoe){
        boolean isEnough = false;
        boolean isEnd = false;
        int countOfIterations = 0;
        if (!_cartRepository.findAll().isEmpty()) {
            //int countOfEqualCartProducts = 0;
            //CartProduct cartProduct = new CartProduct();
            for (CartProduct entity : _cartRepository.findAll()) {
                if (entity.product.id.equals(shoeProduct.id) && entity.product.size.equals(shoeProduct.size)) {
                    entity.quantity++; 
                    _cartRepository.save(entity); 
                    int i = 0;
                    isEnd = true;
                }
                countOfIterations++;
                if (countOfIterations == _cartRepository.findAll().size() && isEnd == false) {
                    isEnough = true;
                }

                if(isEnough){
                    CartProduct cartProduct = new CartProduct();
                    cartProduct.product = shoeProduct;
                    cartProduct.quantity++;
                    _cartRepository.save(cartProduct);
                    isEnd = true;
                }

                if(isEnd){
                    for (Sizes size : shoe.sizes) {
                        if (size.size.equals(shoeProduct.size) && size.isInStock) {
                            size.quantity--;
                            if (size.quantity == 0) {
                                size.isInStock = false;
                            }
                            _shoeRepository.save(shoe);
                            int r = 0;
                        }
                    }
                    break;
                }
            }            
        }
        else {
            CartProduct cartProduct = new CartProduct();
            cartProduct.product = shoeProduct;
            cartProduct.quantity++;
            shoe.sizes.get(shoe.sizes.indexOf(shoeProduct.size)).quantity--;
            //_shoeRepository.save(shoe);
            //_cartRepository.save(cartProduct);
            int i = 0;
            i = 5;
        }
    }

    public Integer totalPriceSummary(){
        int priceSummary = 0;
        for (CartProduct cartProduct : _cartRepository.findAll()) {
            priceSummary += cartProduct.quantity * cartProduct.product.price;
        }
        return priceSummary;
    }*/

    public void deleteFavoriteDealForUser(String id, HttpServletRequest request){
        if (id != null) {
            boolean isFound = false;
            boolean deleted = false;
            var currentUser = _userRepository.findByEmail(request.getUserPrincipal().getName());
            var userFavorites = this.GetAllFavorites()
                                        .stream()
                                        .filter(favorite -> favorite.user.email.equals(currentUser.email))
                                        .findAny()
                                        .get();
            for (Shoe shoe : userFavorites.shoes) {
                if (shoe.id.equals(id)) {
                    userFavorites.shoes.remove(shoe);
                    isFound = true;
                    if (userFavorites.shoes.size() == 0 && userFavorites.clothes.size() == 0) {
                        this._favoritesRepository.delete(userFavorites);
                        deleted = true;
                        break;
                    }
                    if (!deleted) {
                        this._favoritesRepository.save(userFavorites); 
                        break;
                    }
                }
            }
            if (!isFound) {
                for (Clothes cloth : userFavorites.clothes) {
                    if (cloth.id.equals(id)) {
                        userFavorites.clothes.remove(cloth);

                        if (userFavorites.shoes.size() == 0 && userFavorites.clothes.size() == 0) {
                            this._favoritesRepository.delete(userFavorites);
                            deleted = true;
                            break;
                        }
                        if (!deleted) {
                            this._favoritesRepository.save(userFavorites); 
                            break;
                        }
                    }
                }      
            }
        }
    }

    public Collection<SliderShoes> divideShoesByParts() {
        var shoes = this.getAll().stream().filter(shoe -> shoe.isAuctionOffer != true).toList();
        Collection<SliderShoes> sliderShoes = new ArrayList<>();

        if (shoes.size() >= 9) {
            SliderShoes slideShoes = new SliderShoes();
            for (int i = 1; i < 10; i++) {
    
                slideShoes.shoesParts.add(shoes.get(i-1));
    
                if (i % 3 == 0) {
                    sliderShoes.add(slideShoes);
                    slideShoes = new SliderShoes();
                }
            }  
        }
        return sliderShoes;
    } 
}
