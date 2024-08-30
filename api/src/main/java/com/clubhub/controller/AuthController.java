package com.clubhub.controller;

import com.clubhub.dto.LoginDTO;
import com.clubhub.dto.UserDTO;
import com.clubhub.service.UserService;
import com.clubhub.validation.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * A Controller for handling logging in registering and logging out
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    RegisterValidator registerValidator = new RegisterValidator();

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for class
     * This is where all Auto wired dependencies go
     */
    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Login User Endpoint
     * @param loginDTO carries the JSON from front-end to end-point
     * @return a ResponseEntity(JSON) with the result of the login
     */
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginDTO loginDTO) {
        System.out.println("POST /login");

        // Form Validation

        // Check If Login Valid

        // Generate Token

        // Response

        return ResponseEntity.ok(loginDTO);
    }

    /**
     * Register User Endpoint
     * @param userDTO carries the JSON from front-end to end-point
     * @return a ResponseEntity(JSON) with the result of the registration
     */
    @PostMapping("/register")
    public ResponseEntity<?> doRegister(@RequestBody UserDTO userDTO) {
        System.out.println("POST /register");

        userDTO.response = new HashMap<>();

        // Call Form Validation
        if (registerValidator.validateFormData(userDTO)) {
            return ResponseEntity.status(400).body(userDTO.response);
        }

        // Check If Email Already In Use
        if (!userService.checkEmailAvailable(userDTO)) {
            return ResponseEntity.status(400).body(userDTO.response);
        }

        // Hash Password
        userDTO.hashedPassword = passwordEncoder.encode(userDTO.password);

        // Register User
        userService.createUser(userDTO);

        // Handle Response
        return ResponseEntity.ok(userDTO.response);
    }

    @DeleteMapping("/logout")
    public String doLogOut() {
        return "Successfully Logged Out";
    }



}
