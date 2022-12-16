package com.example.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("/index")
    public String viewIndexPage(){
        return "index";
    }

    @GetMapping("/error")
    public String errorPage(){
        System.out.println("wait here");
        return "index";
    }

}
