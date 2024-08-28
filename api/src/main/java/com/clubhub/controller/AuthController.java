package com.clubhub.controller;

import org.springframework.web.bind.annotation.*;

/**
 * A Controller for handling logging in registering and logging out
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    /**
     * Constructor for class
     * This is where all Auto wired dependencies go
     */
    public AuthController() {}

    @GetMapping("/login")
    public String getLogin() {
        return "Login Page";
    }

    @PostMapping("/login")
    public String doLogin() {
        return "Successfully logged user in";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "Register Page";
    }

    @PostMapping("/register")
    public String doRegister() {
        return "Successfully Registered";
    }

    @DeleteMapping("/logout")
    public String doLogOut() {
        return "Successfully Logged Out";
    }



}
