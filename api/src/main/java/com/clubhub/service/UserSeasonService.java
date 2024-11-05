package com.clubhub.service;

import com.clubhub.repository.UserSeasonRepository;
import org.springframework.stereotype.Service;

/**
 * A service layer for all things related to GameRecords
 */
@Service
public class UserSeasonService {


    private final UserSeasonRepository userSeasonRepository;

    public UserSeasonService(UserSeasonRepository userSeasonRepository) {
        this.userSeasonRepository = userSeasonRepository;
    }
}
