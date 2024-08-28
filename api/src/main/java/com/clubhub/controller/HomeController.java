package com.clubhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A hello world controller to help get set up
 */
@RestController
@RequestMapping("/api")
public class HomeController {

    /**
     * Constructor for class
     * This is where all Auto wired dependencies go
     */
    public HomeController() {}


    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

}
