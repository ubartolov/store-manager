package com.example.storemanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainPageController {

    @RequestMapping("/")
    public String mainPage() {
        return "mainpage";
    }

    @RequestMapping("/warehousespage")
    public String warehousePage() {
        return "warehousespage";
    }
}
