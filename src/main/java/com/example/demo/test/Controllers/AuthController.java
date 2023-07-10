package com.example.demo.test.Controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.test.Models.entities.CartProduct;
import com.example.demo.test.Models.entities.Favorite;
import com.example.demo.test.Models.entities.Privilege;
import com.example.demo.test.Models.entities.Role;
import com.example.demo.test.Models.entities.User;
import com.example.demo.test.Repositories.CartRepository;
import com.example.demo.test.Repositories.FavoritesRepository;
import com.example.demo.test.Repositories.authRepositories.PrivilegeRepository;
import com.example.demo.test.Repositories.authRepositories.RoleRepository;
import com.example.demo.test.Repositories.authRepositories.UserRepository;

@Controller
public class AuthController {

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private CartRepository _cartRepository;

    @Autowired
    private FavoritesRepository _favoritesRepository;

    @Autowired
    private RoleRepository _roleRepository;

    @Autowired
    private PrivilegeRepository _privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String mainControllerUrl = "http://localhost:8080";

    public AuthController() {
        
    }

    @GetMapping("/user/register")
    @PreAuthorize("!hasRole('ROLE_ADMIN') AND !hasRole('ROLE_USER')")
    public String showRegistrationForm(Model model) { return "Auth_pages/register_form.html"; }   

    @GetMapping("/user/success")
    public String successfulAuth(Model model) { return "register_success.html"; }
    
    @PostMapping("/user/register")
    public RedirectView registerNewUserAccount(String username, String email, String password) throws Exception {
 
        if (emailExist(email)) {
            throw new Exception
              ("There is an account with that email adress: " + email);
        }
        else if(usernameExist(username)){
            throw new Exception
              ("There is an account with that username: " + username);
        }
        User user = new User();
    
        user.username = username;
        user.password = passwordEncoder.encode(password);
        user.email = email;
        user.enabled = true;
    
        user.roles = Arrays.asList(_roleRepository.findByName("ROLE_USER"));
        _userRepository.save(user);
        return redirectView(mainControllerUrl + "/user/success");
    }

    public boolean emailExist(String email){
        User user = _userRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public boolean usernameExist(String username){
        User user = _userRepository.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }


    @PostMapping("/admin/register")
    public RedirectView processRegister(String username, String email, String password, HttpServletRequest request) {

        Privilege readPrivilege
          = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
          = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Role adminRole = _roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.username = username;
        user.password = passwordEncoder.encode(password);
        user.email = email;
        user.roles = Arrays.asList(adminRole);
        user.enabled = true;
        _userRepository.save(user);

        return redirectView("http://localhost:8080/user/login");
    }
    
    public Privilege createPrivilegeIfNotFound(String name) {
 
        Privilege privilege = _privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            _privilegeRepository.save(privilege);
        }
        return privilege;
    }

    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
 
        Role role = _roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            _roleRepository.save(role);
        }
        return role;
    }
    
    @GetMapping("/user/login")
    public String showLoginForm(Model model) {
     
        return "Auth_pages/login_form.html";
    }

    @PostMapping("/user/{id}/delete")
    public RedirectView deleteUser(@PathVariable String id) {
        var userEntity = _userRepository.findById(id);
        if (userEntity.isPresent()) {
            Optional<CartProduct> cartForDeletedUser = _cartRepository.findAll().stream().filter(cart -> cart.user.id.equals(userEntity.get().id)).findAny();
            if (cartForDeletedUser.isPresent()) {
                _cartRepository.delete(cartForDeletedUser.get());    
            }

            Optional<Favorite> favoriteForDeletedUser = _favoritesRepository.findAll().stream().filter(fav -> fav.user.id.equals(userEntity.get().id)).findAny();
            if (favoriteForDeletedUser.isPresent()) {
                _favoritesRepository.delete(favoriteForDeletedUser.get());    
            }

            _userRepository.delete(userEntity.get());
        }

        return redirectView(mainControllerUrl + "/admin");
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = _userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
     
        return "users";
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }

}
