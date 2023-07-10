package com.example.demo.test.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller()
public class BookController {

    @GetMapping("/books")
    public String returnBook(){
        return "book";
    }
}
