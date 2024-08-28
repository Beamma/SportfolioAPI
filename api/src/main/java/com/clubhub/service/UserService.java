package com.clubhub.service;

import com.clubhub.entity.User;
import com.clubhub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

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

}
