package com.example.demo.test.controllers;

import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.test.services.ClothesService;
import com.example.demo.test.services.FilesService;
import com.example.demo.test.services.ShoeService;
import com.example.demo.test.services.UserService;
import com.example.demo.test.models.classModels.FileDB;
import com.example.demo.test.models.classModels.Filter;
import com.example.demo.test.models.classModels.Sizes;
import com.example.demo.test.models.entities.Shoe;
import com.example.demo.test.models.enums.Brands;
import com.example.demo.test.models.enums.Colors;
import com.example.demo.test.models.enums.Conditions;

import java.util.List;

@Controller
@RequestMapping(value = {"{lang}/sneakers", "/sneakers"})
public class OfferController {

    private ShoeService _shoeService;

    @Autowired
    private UserService _userService;

    @Autowired
    private ClothesService _clothesService;

    private final String mainControllerUrl = "http://localhost:8080/sneakers";

    private final String mainBgControllerUrl = "http://localhost:8080/bg/sneakers";

    @Autowired
    public OfferController(FilesService filesService, ShoeService shoeService) {
        this._shoeService = shoeService;
        _shoeService.pageFilters = new ArrayList<>();
        _shoeService.addFilters();
        _shoeService.currentFilteredList = shoeService.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
        _shoeService.filterIsRemoved = false;
    }

    @GetMapping
    public String getSneakers(Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();

        List<Filter> appliedToPageFilters = new ArrayList<>();
        model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("offers", _shoeService.currentFilteredList);
            
            for (Filter filter : _shoeService.pageFilters) {
                if (!filter.value.equals("")) {
                    if (filter.type.equals("brand")) {
                        model.addAttribute("brandFilter", filter.value.toLowerCase());
                    }
    
                    else if (filter.type.equals("color")) {
                        model.addAttribute("colorFilter", filter.value.toLowerCase());
                    }
    
                    else if(filter.type.equals("size")){
                        model.addAttribute("sizeFilter", filter.value);
                        /*if (!filter.value.startsWith("N")) {
                            String result = "N=" + filter.value;
                            filter.value = result;                    
                        }*/
                    }
    
                    else if (filter.type.equals("price")) {
                        model.addAttribute("priceFilter", filter.value);
                        /*if (!filter.value.startsWith("$")) {
                            String[] filterParts = filter.value.split("-");
                            String firstPart = "$" + filterParts[0];
                            String secondPart = "-$" + filterParts[1];
                            String result = firstPart.concat(secondPart);
                            filter.value = result;      
                        }*/
                    }
    
                    appliedToPageFilters.add(filter);
                }
            }   
        model.addAttribute("filters", appliedToPageFilters);
        model.addAttribute("brands", Brands.values());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        //_carService.setLanguage(request, model);
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());

        _clothesService.removeAllFilters();

        return "User_pages/sneakers_list.html";
    }


    @PostMapping("/brand")
    public RedirectView filterSneakersBrand(Model model, 
                                      @RequestParam("brand") String brand,
                                      HttpServletRequest request){
        _shoeService.placeBrandFilter(brand);
                                        
        //_carService.setLanguage(request, model); 
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/color")
    public RedirectView filterSneakersCategory(Model model, 
                                      @RequestParam(name = "color", defaultValue = "", required = false) String color,
                                      HttpServletRequest request){
        _shoeService.placeColorFilter(color);
        //_carService.setLanguage(request, model);
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian()); 

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/size")
    public RedirectView filterSneakersSize(Model model, 
                                      @RequestParam("size") String size,
                                      HttpServletRequest request){
        _shoeService.placeSizeFilter(size);
        //_carService.setLanguage(request, model);
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian()); 

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }     
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/price")
    public RedirectView filterSneakersPrice(Model model, 
                                      @RequestParam(name = "price", defaultValue = "", required = false) String price,
                                      HttpServletRequest request){
        _shoeService.placePriceFilter(price);
        //_carService.setLanguage(request, model); 
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/clear")
    public RedirectView detachFilter(Model model, @RequestParam("filter") String filter, HttpServletRequest request){
        for (Filter filt : _shoeService.pageFilters) {
            if (filt.type.toLowerCase().equals(filter.toLowerCase())) {
                filt.value = "";
                _shoeService.filterIsRemoved = true;
            }
        }

        if (_shoeService.filterIsRemoved) {
            _shoeService.currentFilteredList = _shoeService.getAll().stream().filter(shoe -> shoe.isAuctionOffer == false).toList();
            _shoeService.applyCurrentFilters(_shoeService.pageFilters);
            _shoeService.filterIsRemoved = false;
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    /* 
    @PostMapping
    public RedirectView filterSneakers(Model model, 
                                      @RequestParam("brand") String brand, 
                                      @RequestParam("category") String category,
                                      @RequestParam("size") String size,
                                      @RequestParam("price") String price, 
                                      HttpServletRequest request){
        
        if (filterIsRemoved) {
            applyCurrentFilters(pageFilters);
            filterIsRemoved = false;
        }
        
        if (brand != null) {
            if (brandFilterPlaced) {
                removeFilter(0, pageFilters);
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
            pageFilters.add(brandFilter);
        }

        if (category != null) {
            if (sizeCategoryFilterPlaced) {
                removeFilter(1, pageFilters);
                currentFilteredList = _dealService.GetAll();
            }
            pageFilters.get(1).value = category;
            applyCurrentFilters(pageFilters);
            sizeCategoryFilterPlaced = true;
            
            currentFilterList = currentFilterList
            .stream()
            .filter(deal -> deal.car.model.toLowerCase().equals(category.toLowerCase()))
            .collect(Collectors.toList());
            sizeCategoryFilter = new Filter("category", category.toLowerCase()); 
        } 
        if (size != null) {
            if (shoeSizeFilterPlaced) {
                removeFilter(2, pageFilters);
                currentFilteredList = _dealService.GetAll();
            }
            pageFilters.get(2).value = size;
            applyCurrentFilters(pageFilters);
            shoeSizeFilterPlaced = true;
        }

        model.addAttribute("offers", currentFilteredList);
        int i = 0;
        i = 5;
        
        return redirectView(mainControllerUrl); 
    } 
    */

    /*@RequestMapping(value = "/{category}", method = RequestMethod.POST)
    public String filterSneakersCategory(Model model, @RequestParam("category") String category, HttpServletRequest request){
        if (category != null) {
            currentFilterList = currentFilterList
            .stream()
            .filter(car -> car.model.toLowerCase().equals(category.toLowerCase()))
            .collect(Collectors.toList());
            sizeCategoryFilter = category.toLowerCase(); 
        }
        return "sneakers_list.html";
    }*/

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


    /*
    TO DELETE
    @PostMapping("/edit/{id}")
    public RedirectView edit(@PathVariable String id, String brand, String model, int distanceInKm, String yearOfProduction, String imageUrl){
        Car car = this._carService.GetById(id);
        car.brand = brand;
        car.model = model;
        car.distanceInKm = distanceInKm;
        car.yearOfProduction = yearOfProduction;
        car.imageUrl = imageUrl;
        this._carService.edit(car);
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/delete/{id}")
    public RedirectView delete(@PathVariable String id){
        this._carService.Delete(id);
        return redirectView(mainControllerUrl);
    }*/

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
    
}
