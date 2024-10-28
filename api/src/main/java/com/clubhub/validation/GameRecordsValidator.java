package com.clubhub.validation;

import com.clubhub.requestBody.AddBulkSeasonRequest;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class GameRecordsValidator {

    private static final int MAX_SEASON_GAMES = 30;
    private static final int MIN_SEASON_GAMES = 1;

    private static final String INVALID_GAMES_COUNT_ERROR = "Player Game Counts Must Be Between 1 And 30";



    /**
     * Check that the request for bulk adding games is valid
     * @param request object, containing all information from request
     * @param response a key value json response
     * @return true if valid, false if error
     */
    public Boolean validateAddBulkSeasonRequest(List<AddBulkSeasonRequest> request, Map<String, Object> response) {

        for (AddBulkSeasonRequest item: request) {
            // Check amount of games is less than 31 greater than 0
            if (!validateSeasonGamesCount(item, response)) {
                return false;
            }

            // Check playerId is valid

            // Check season is between 1850 and current year

            // For now, we will allow competition to be any positive number

            // Check for duplicates possibly?

            // Possibly have to check that it's not already in db, otherwise prompt them to do a put request?
        }

        return true;
    }

    /**
     * Check that the count for Games for the season is valid
     * @param request object, containing all information from request
     * @param response a key value json response
     * @return true if valid, false if error
     */
    private Boolean validateSeasonGamesCount(AddBulkSeasonRequest request, Map<String, Object> response) {

        if (request.count > MAX_SEASON_GAMES || request.count < MIN_SEASON_GAMES) {
            response.put("requestError", INVALID_GAMES_COUNT_ERROR);
            return false;
        }

        return true;
    }
}
