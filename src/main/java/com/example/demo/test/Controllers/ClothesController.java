package com.example.demo.test.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.test.models.classModels.FileDB;
import com.example.demo.test.models.classModels.Filter;
import com.example.demo.test.models.classModels.Sizes;
import com.example.demo.test.models.entities.Clothes;
import com.example.demo.test.models.entities.Favorite;
import com.example.demo.test.models.enums.ClothesBrands;
import com.example.demo.test.models.enums.Colors;
import com.example.demo.test.repositories.ClothesRepository;
import com.example.demo.test.services.ClothesService;
import com.example.demo.test.services.ShoeService;
import com.example.demo.test.services.UserService;

@Controller
@RequestMapping(value = {"{lang}/clothes", "/clothes"})
public class ClothesController {

    @Autowired
    private ShoeService _shoeService;

    @Autowired
    private UserService _userService;

    private ClothesRepository _clothesRepository;

    private ClothesService _clothesService;

    private final String mainControllerUrl = "http://localhost:8080/clothes";

    private final String mainBgControllerUrl = "http://localhost:8080/bg/clothes";

    
    @Autowired
    public ClothesController(ClothesRepository clothesRepository, ClothesService clothesService) {
        this._clothesRepository = clothesRepository;
        this._clothesService = clothesService;
        clothesService.pageFilters = new ArrayList<>();
        clothesService.addFilters();
        clothesService.currentFilteredList = clothesRepository.findAll();
        clothesService.filterIsRemoved = false;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String getClothes(Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();

        List<Filter> appliedToPageFilters = new ArrayList<>();
        model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("offers", _clothesService.currentFilteredList);
            
            for (Filter filter : _clothesService.pageFilters) {
                if (!filter.value.equals("")) {
                    if (filter.type.equals("brand")) {
                        model.addAttribute("brandFilter", filter.value.toLowerCase());
                    }
    
                    else if (filter.type.equals("gender")) {
                        model.addAttribute("genderFilter", filter.value.toLowerCase());
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
        model.addAttribute("brands", ClothesBrands.values());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());

        _shoeService.removeAllFilters();

        return "user_pages/clothes_list.html";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public String getClothesById(@PathVariable String id, Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();
        var cloth = this._clothesRepository.findById(id);
        if (cloth.isPresent()) {
            model.addAttribute("cloth", cloth.get());
            Optional<Favorite> favForUser = _shoeService.GetAllFavorites().stream().filter(fav -> fav.user.id.equals(_userService.getCurrentUser().id)).findAny();
            if (favForUser.isPresent()) {
                model.addAttribute("isAddedToFavorites", favForUser.get().clothes.stream().filter(cl -> cl.id.equals(cloth.get().id)).findAny().isPresent()); 
            }                   
        }

        var relatedClothes = _clothesRepository.findAll()
                                       .stream()
                                       .filter(c -> {
                                            if (c.id.equals(id)) {
                                                return false;
                                            }
                                            return true;
                                        })
                                        .filter(c -> c.brand.equals(cloth.get().brand))
                                        .limit(5)
                                        .collect(Collectors.toList());
                                                                 
        model.addAttribute("relatedClothes", relatedClothes);
        model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());

        _clothesService.removeAllFilters();
        
        return "user_pages/product_pages/clothes/clothes_product_page.html";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RedirectView create(ClothesBrands brand, String model, String description, 
                               String releaseDate, String[] sizes, 
                               Colors color, String colorSpecification, String gender, Integer price,
                               @RequestParam("images") MultipartFile[] images){
        try {
            String clothesImagesPath = System.getProperty("clothes-images.path");

            List<Sizes> sizesInStock = new ArrayList<>();
            if (sizes != null) {
                _shoeService.addSizesForShoe(sizes, sizesInStock);    
            }
            
            String newClothesDirectoryName = _shoeService.concatenate(brand.toString().toLowerCase(), "_", model.toLowerCase().replace(" ", "-"), "_", colorSpecification.toLowerCase().replace(" ", "-").replace("/", "-"));
            File newClothesImagesDirectory = new File(clothesImagesPath, newClothesDirectoryName);
            
            
            if (newClothesImagesDirectory.mkdir()) {

                List<FileDB> listOfImages = new ArrayList<>();     

                    File manyImagesDirectory = new File(newClothesImagesDirectory, "images");
                    if (manyImagesDirectory.mkdir()) {
                        for (MultipartFile image : images) {
                            String imageName = image.getOriginalFilename();
                            String imageUrl = "clothes-images/" + newClothesDirectoryName + "/images/" + imageName;
                            FileDB fileDB = new FileDB(imageName, image.getContentType(), imageUrl);
                            listOfImages.add(fileDB);                      
                            this._shoeService.uploadImage(image, imageName, manyImagesDirectory);    
                        }     
                    }

                Clothes cloth = new Clothes(brand, model, description, releaseDate, sizesInStock, color, colorSpecification, gender, price, listOfImages);

                _clothesRepository.save(cloth);
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

    //filters -->

    @PostMapping("/brand")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView filterSneakersBrand(Model model, 
                                      @RequestParam("brand") String brand,
                                      HttpServletRequest request){
        _clothesService.placeBrandFilter(brand);
 
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());  
        
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/gender")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView filterSneakersCategory(Model model, 
                                      @RequestParam(name = "gender", defaultValue = "", required = false) String gender,
                                      HttpServletRequest request){

        _clothesService.placeGenderFilter(gender);

        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/size")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView filterSneakersSize(Model model, 
                                      @RequestParam("size") String size,
                                      HttpServletRequest request){
        _clothesService.placeSizeFilter(size);

        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/price")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView filterSneakersPrice(Model model, 
                                      @RequestParam(name = "price", defaultValue = "", required = false) String price,
                                      HttpServletRequest request){
        _clothesService.placePriceFilter(price);

        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());
        
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    @PostMapping("/clear")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public RedirectView detachFilter(Model model, @RequestParam("filter") String filter, HttpServletRequest request){

        _clothesService.detachFilter(filter);

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(mainBgControllerUrl);
        }
        return redirectView(mainControllerUrl);
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
