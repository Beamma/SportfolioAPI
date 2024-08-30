package com.clubhub.service;

import com.clubhub.dto.UserDTO;
import com.clubhub.entity.User;
import com.clubhub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String ERROR_EMAIL_IN_USE = "This Email Is Already In Use, Try Logging In";

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor for the user service for autowired dependencies
     * @param userRepository a JPA repository for the User entity
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Check if an email is in use
     * @param userDTO a DTO carrying all the register form data
     * @return if the email is in use or not
     */
    public Boolean checkEmailAvailable(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.email) == null) {
            return true;
        }

        userDTO.response.put("emailError", ERROR_EMAIL_IN_USE);

        return false;
    }

    /**
     * Takes a userDTO and creates a user in the database
     * @param userDTO a DTO carrying all the register form data
     */
    public void createUser(UserDTO userDTO) {
        User user = new User(userDTO.firstName, userDTO.lastName, userDTO.email, userDTO.hashedPassword);
        userRepository.save(user);
    }

    /**
     * Encrypt a users password using BCrypt password encoder
     * @param userDTO a DTO carrying all the register form data
     */
    public void encryptPassword(UserDTO userDTO) {
        userDTO.hashedPassword = passwordEncoder.encode(userDTO.password);
    }

    public boolean checkPassword(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.email);
        if (user == null) {
            userDTO.response.put("loginError", "Incorrect Email Or Password");
            return false;
        }

        if (!passwordEncoder.matches(userDTO.password, user.getPassword())) {
            userDTO.response.put("loginError", "Incorrect Email Or Password");
            return false;
        }

        return true;
    }

}
