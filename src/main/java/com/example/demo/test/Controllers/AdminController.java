package com.example.demo.test.Controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.test.Models.classModels.FileDB;
import com.example.demo.test.Models.enums.Brands;
import com.example.demo.test.Models.enums.ClothesBrands;
import com.example.demo.test.Repositories.ClothesRepository;
import com.example.demo.test.Repositories.ShoeRepository;
import com.example.demo.test.Repositories.authRepositories.UserRepository;
import com.example.demo.test.Services.CartService;
import com.example.demo.test.Services.ClothesService;
import com.example.demo.test.Services.ShoeService;
import com.example.demo.test.Services.UserService;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private UserService _userService;

    @Autowired
    private ShoeService _shoeService;

    @Autowired
    private CartService _cartService;

    @Autowired
    private ClothesService _clothesService;

    @Autowired
    private ShoeRepository _shoeRepository;

    @Autowired
    private ClothesRepository _clothesRepository;

    private final String samePageRedirectUrl = "http://localhost:8080/admin";

    private final String samePageBgRedirectUrl = "http://localhost:8080/bg/admin";

    public AdminController() {

    }

    @GetMapping(value = {"/bg/admin", "/admin"})
    public String returnAdminPage(Model model, HttpServletRequest request){
        this._shoeService.currentControllerUrl = request.getRequestURI().toString();
        //Authentication user = SecurityContextHolder.getContext().getAuthentication();
        var shoes = _shoeService.getAll();

        model.addAttribute("shoes", shoes);
        model.addAttribute("clothes", _clothesRepository.findAll());
        model.addAttribute("countOfFavorites", _shoeService.getCountOfFavorites());
        model.addAttribute("username", _userService.getCurrentUser().username);
        model.addAttribute("adminUser", _userService.getAdminUser());
        model.addAttribute("isAdmin", _userService.isAdmin());
        model.addAttribute("carsCount", shoes.size());
        model.addAttribute("users", _userService.getAllUsers());
        model.addAttribute("dailyTurnOver", _cartService.sumTotalTurnOverForToday());
        model.addAttribute("dailyOrders", _cartService.getAllOrdersForToday());

        //_carService.setLanguage(request, model);
        model.addAttribute("isBgLang", _shoeService.isLanguageBulgarian());

        //clear filters on clothes page when go to admin page
        _shoeService.removeAllFilters();
        _clothesService.removeAllFilters();

        return "admin/admin_page.html";
    }

    @PostMapping("/user/{id}/edit")
    public RedirectView editAdminUser(@PathVariable String id, String username){
        var user = _userRepository.findById(id);
        if (user.isPresent()) {
            var userEntity = user.get();
            userEntity.username = username;
            _userRepository.save(userEntity);
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(samePageBgRedirectUrl);
        }

        return redirectView(samePageRedirectUrl);
    }

    @PostMapping("/shoes/{id}/delete")
    public RedirectView deleteShoe(@PathVariable String id){
        var shoe = _shoeService.GetById(id);
        if (shoe != null) {
            String imagesPath = System.getProperty("images.path");
            String shoeDirectoryName = shoe.mainImageUrl.split("/")[0];
            //String shoeDirectoryName = _shoeService.concatenate(shoe.brand.toString().toLowerCase(), "_", shoe.model.toLowerCase().replace(" ", "-"), "_", shoe.colorSpecification.toLowerCase().replace(" ", "-"));
            File shoesDirectory = new File(imagesPath, shoeDirectoryName);
            if (shoesDirectory.exists()) {
                if (deleteDirectory(shoesDirectory)) {
                    _shoeService.Delete(id);         
                }
            }
        }
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(samePageBgRedirectUrl);
        }
        return redirectView(samePageRedirectUrl);
    }

    public boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete(); 
    }

    @PostMapping("/shoes/{id}/edit")
    public RedirectView editShoe(@PathVariable String id, Brands brand, String model, Integer price, String releaseDate, @RequestParam("mainImage") MultipartFile mainImage){
        var shoe = _shoeService.GetById(id);
        if (shoe != null) {
            String imagesPath = System.getProperty("images.path");
            String[] shoeDirectoryNames = shoe.mainImageUrl.split("/");
            String shoeDirectoryName = shoeDirectoryNames[0].concat("/".concat(shoeDirectoryNames[1]));

            //String shoeDirectoryName = _shoeService.concatenate(shoe.brand.toString().toLowerCase(), "_", shoe.model.toLowerCase().replace(" ", "-"), "_", shoe.colorSpecification.toLowerCase().replace(" ", "-"));
            File shoesDirectory = new File(imagesPath, shoeDirectoryName);
            if (shoesDirectory.exists() && !mainImage.isEmpty()) {
                if (deleteFileFromDirectory(shoesDirectory)) {
                    String mainImageName = StringUtils.cleanPath(mainImage.getOriginalFilename());
                    this._shoeService.uploadImage(mainImage, mainImageName, shoesDirectory);

                    var mainImageUrl = shoeDirectoryName + "/" + mainImageName;
                    shoe.mainImageUrl = mainImageUrl;
                }
            }
            shoe.brand = brand;
            shoe.model = model;
            shoe.price = price;
            shoe.releaseDate = releaseDate;
            _shoeRepository.save(shoe);
        }
        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(samePageBgRedirectUrl);
        }

        return redirectView(samePageRedirectUrl);
    }

    public boolean deleteFileFromDirectory(File directoryWithFiles) {
        File[] allContents = directoryWithFiles.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                return file.delete();
            }
        }
        return false; 
    }


    @PostMapping("/clothes/{id}/edit")
    public RedirectView editCloth(@PathVariable String id, ClothesBrands brand, String model, Integer price, String releaseDate, @RequestParam("images") MultipartFile[] images){
        var cloth = _clothesRepository.findById(id);
        if (cloth.isPresent()) {
            String imagesPath = System.getProperty("clothes-images.path");
            String[] clothDirectoryNames = cloth.get().images.get(0).imagePath.split("/");
            String clothDirectoryName = clothDirectoryNames[1].concat("/".concat(clothDirectoryNames[2]));

            //String shoeDirectoryName = _shoeService.concatenate(shoe.brand.toString().toLowerCase(), "_", shoe.model.toLowerCase().replace(" ", "-"), "_", shoe.colorSpecification.toLowerCase().replace(" ", "-"));
            File clothesDirectory = new File(imagesPath, clothDirectoryName);
            if (clothesDirectory.exists() && !images[0].isEmpty()) {
                if (deleteFileFromDirectory(clothesDirectory)) {
                    List<FileDB> listOfImages = new ArrayList<>(); 
                    for (MultipartFile image : images) {
                        String imageName = image.getOriginalFilename();
                        String imageUrl = "clothes-Images/" + clothDirectoryNames[1] + "/images/" + imageName;
                        FileDB fileDB = new FileDB(imageName, image.getContentType(), imageUrl);
                        listOfImages.add(fileDB);

                        cloth.get().images = listOfImages;
                                              
                        this._shoeService.uploadImage(image, imageName, clothesDirectory);    
                    } 
                    //String mainImageName = StringUtils.cleanPath(mainImage.getOriginalFilename());
                    //this._shoeService.uploadImage(mainImage, mainImageName, shoesDirectory);
                }
            }
            cloth.get().brand = brand;
            cloth.get().model = model;
            cloth.get().price = price;
            cloth.get().releaseDate = releaseDate;
            _clothesRepository.save(cloth.get());
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(samePageBgRedirectUrl);
        }

        return redirectView(samePageRedirectUrl);
    }


    @PostMapping("/clothes/{id}/delete")
    public RedirectView deleteCloth(@PathVariable String id){
        var cloth = _clothesRepository.findById(id);
        if (cloth.isPresent()) {
            String clothesImagesPath = System.getProperty("clothes-images.path");
            String clothDirectoryName = cloth.get().images.get(0).imagePath.split("/")[1];
            //String shoeDirectoryName = _shoeService.concatenate(shoe.brand.toString().toLowerCase(), "_", shoe.model.toLowerCase().replace(" ", "-"), "_", shoe.colorSpecification.toLowerCase().replace(" ", "-"));
            File clothesDirectory = new File(clothesImagesPath, clothDirectoryName);
            if (clothesDirectory.exists()) {
                if (deleteDirectory(clothesDirectory)) {
                    _clothesRepository.delete(cloth.get());        
                }
            }
        }

        if (_shoeService.isLanguageBulgarian()) {
            return redirectView(samePageBgRedirectUrl);
        }

        return redirectView(samePageRedirectUrl);
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
