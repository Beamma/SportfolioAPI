package com.clubhub.service;

import com.clubhub.dto.ClubUpdateDTO;
import com.clubhub.dto.UserDTO;
import com.clubhub.entity.User;
import com.clubhub.repository.UserRepository;
import com.clubhub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service layer for all things related to the user entity
 */
@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

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

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
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

    /**
     * Get the user who has made the request
     * @param token, the Authorization bearer token
     * @return the user who is currently making the request.
     */
    public User getCurrentUser(String token) {
        String userEmail = jwtUtil.extractUsername(token);

        return userRepository.findByEmail(userEmail);
    }

    /**
     * Check if the current user is authorized to update requests
     * @param clubUpdateDTO a DTO to carry all the required information
     * @return true if successful, false otherwise
     */
    public boolean userAllowedToUpdateClubRequestStatus(ClubUpdateDTO clubUpdateDTO) {
        User user = getCurrentUser(clubUpdateDTO.getToken());

        String role = null;
        Long userClubId = null;

        if (user.getClubMember() != null) {
            role = user.getClubMember().getRole();
            userClubId = user.getClubMember().getClub().getId();
        }


        // If the member doesn't yet belong to a club
        if (role == null) {
            return clubUpdateDTO.getRequestedStatus().equals("canceled");
        }

        // If the user is a member of their respective club
        if (role.equals("MEMBER") && userClubId.equals(clubUpdateDTO.getClub().getId())) {
            return clubUpdateDTO.getRequestedStatus().equals("quit");
        }

        // If the user is an admin of their respective club
        if (role.equals("ADMIN") && userClubId.equals(clubUpdateDTO.getClub().getId())) {
            return clubUpdateDTO.getRequestedStatus().equals("removed") || clubUpdateDTO.getRequestedStatus().equals("accepted");
        }

        // If the user is root, they can do anything
        return role.equals("ROOT");
    }

}
