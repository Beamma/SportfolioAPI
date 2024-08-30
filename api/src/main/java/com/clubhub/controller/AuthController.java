package com.clubhub.controller;

import com.clubhub.dto.UserDTO;
import com.clubhub.security.JwtUtil;
import com.clubhub.service.UserService;
import com.clubhub.validation.AuthenticationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * A Controller for handling logging in registering and logging out
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    AuthenticationValidator authenticationValidator = new AuthenticationValidator();

    private final UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Constructor for class
     * This is where all Auto wired dependencies go
     */
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Login User Endpoint
     * @param userDTO carries the JSON from front-end to end-point
     * @return a ResponseEntity(JSON) with the result of the login
     */
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody UserDTO userDTO) {
        System.out.println("POST /login");

        userDTO.response = new HashMap<>();

        // Form Validation
        if (authenticationValidator.validateLoginData(userDTO)) {
            return ResponseEntity.status(400).body(userDTO.response);
        }

        // Check If Login Valid
        if (!userService.checkPassword(userDTO)) {
            return ResponseEntity.status(401).body(userDTO.response);
        }

        // Generate Token
        userDTO.response.put("token", jwtUtil.generateToken(userDTO.email));

        // Response with user so front end knows what to set user as.
        userDTO.response.put("user", userDTO.email);

        // Response
        return ResponseEntity.ok(userDTO.response);
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
        if (authenticationValidator.validateRegisterData(userDTO)) {
            return ResponseEntity.status(400).body(userDTO.response);
        }

        // Check If Email Already In Use
        if (!userService.checkEmailAvailable(userDTO)) {
            return ResponseEntity.status(400).body(userDTO.response);
        }

        // Hash Password
        userService.encryptPassword(userDTO);

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
