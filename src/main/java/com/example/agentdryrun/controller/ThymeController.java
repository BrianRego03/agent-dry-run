package com.example.agentdryrun.controller;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class ThymeController {

    public ThymeController(){

    }


    
    @GetMapping("/thyme")
    public String thyme(Model model) {

        model.addAttribute("message", "Welcome to my Spring Boot application!");

        return "test";
    }

    @GetMapping("/external")
    public String external(Model model) throws Exception {

        String html = Jsoup.connect("https://example.com/")
                .get()
                .html();

        model.addAttribute("html", html);

        return "external";
    }

}
