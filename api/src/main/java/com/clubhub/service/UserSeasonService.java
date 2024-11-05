package com.clubhub.service;

import com.clubhub.entity.Records.UserSeason;
import com.clubhub.entity.User;
import com.clubhub.repository.UserSeasonRepository;
import com.clubhub.requestBody.AddBulkSeasonRequest;
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

    /**
     * Creates a UserSeason and stores it in the database
     * @param seasonRequest HTTP Request, holding necessary information
     * @param player Player who the season belongs too
     * @return A UserSeason entity if successful, null otherwise
     */
    public UserSeason createUserSeason(AddBulkSeasonRequest seasonRequest, User player) {

        UserSeason userSeason = new UserSeason(seasonRequest.count, player, seasonRequest.season, seasonRequest.competition);

        return userSeasonRepository.save(userSeason);
    }
}
