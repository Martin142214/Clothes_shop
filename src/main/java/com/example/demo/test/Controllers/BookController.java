package com.example.demo.test.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller()
public class BookController {

    @GetMapping("/books")
    public String returnBook(){
        return "book";
    }
}
