package com.clubhub.validation;

import com.clubhub.requestBody.AddBulkSeasonRequest;
import com.clubhub.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class GameRecordsValidator {

    private static final int MAX_SEASON_GAMES = 30;
    private static final int MIN_SEASON_GAMES = 1;

    private static final String INVALID_GAMES_COUNT_ERROR = "Player Game Counts Must Be Between 1 And 30";

    private static final int MIN_SEASON = 1850;

    private static final String INVALID_SEASON_ERROR = "Season Must Be Between 1850 And Cannot Be In The Future";

    private static final String INVALID_COMPETITION_ERROR = "Invalid Season Competition Selection";


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

            // Check season is between 1850 and current year
            if (!validateSeason(item, response)) {
                return false;
            }

            // For now, we will allow competition to be any positive number
            if (!validateCompetition(item, response)) {
                return false;
            }

            // Check for duplicates possibly? TODO

            // Possibly have to check that it's not already in db, otherwise prompt them to do a put request? TODO
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
            response.put("countError", INVALID_GAMES_COUNT_ERROR);
            return false;
        }

        return true;
    }

    /**
     * Check is the season for a record is valid
     * @param request object, containing all information from request
     * @param response a key value json response
     * @return true if valid, false if error
     */
    private Boolean validateSeason(AddBulkSeasonRequest request, Map<String, Object> response) {
        if (request.season > Year.now().getValue() || request.count < MIN_SEASON) {
            response.put("seasonError", INVALID_SEASON_ERROR);
            return false;
        }

        return true;
    }

    /**
     *  Check if the competition for a record is valid
     * @param request object, containing all information from request
     * @param response a key value json response
     * @return true if valid, false if error
     */
    private Boolean validateCompetition (AddBulkSeasonRequest request, Map<String, Object> response) { // TODO Needs to be changed when competitions introduced
        if (request.competition <= 0) {
            response.put("competitionError", INVALID_COMPETITION_ERROR);
            return false;
        }

        return true;
    }
}
