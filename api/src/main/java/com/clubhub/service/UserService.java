package com.clubhub.service;

import com.clubhub.dto.UserDTO;
import com.clubhub.entity.User;
import com.clubhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final String ERROR_EMAIL_IN_USE = "This Email Is Already In Use, Try Logging In";

    private final UserRepository userRepository;

    /**
     * Constructor for the user service for autowired dependencies
     * @param userRepository a JPA repository for the User entity
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get all users on the app
     * @return a list of all users on the app
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Check if an email is in use
     * @param userDTO a DTO carrying all the register form data
     * @return if the email is in use or not
     */
    public Boolean checkEmailAvailable(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.email) == null) {
            userDTO.errors = true;
            userDTO.emailError = ERROR_EMAIL_IN_USE;
            return false;
        }

        return true;
    }

    public void createUser(UserDTO userDTO) {
        User user = new User(userDTO.firstName, userDTO.lastName, userDTO.email, userDTO.hashedPassword);
        user = userRepository.save(user);

        userDTO.id = user.getId();
    }

}
