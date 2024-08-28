package com.clubhub.controller;

import com.clubhub.dto.UserDTO;
import com.clubhub.service.UserService;
import com.clubhub.validation.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A Controller for handling logging in registering and logging out
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    RegisterValidator registerValidator = new RegisterValidator();

    private final UserService userService;

    /**
     * Constructor for class
     * This is where all Auto wired dependencies go
     */
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

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
    public ResponseEntity<?> doRegister(@RequestBody UserDTO userDTO) {
        System.out.println("POST /register");

        // Call Form Validation
        if (registerValidator.validateFormData(userDTO)) {
            return ResponseEntity.status(400).body(userDTO);
        }

        // Check If Email Already In Use
        if (userService.checkEmailAvailable(userDTO)) {
            return ResponseEntity.status(400).body(userDTO);
        }

        // Hash Password
        userDTO.hashedPassword = "HASHED_PASSWORD"; //TODO Actually hash the password

        // Register User
        userService.createUser(userDTO);

        // Handle Response
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/logout")
    public String doLogOut() {
        return "Successfully Logged Out";
    }



}
