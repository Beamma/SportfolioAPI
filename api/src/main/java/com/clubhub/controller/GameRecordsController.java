package com.clubhub.controller;

import com.clubhub.entity.Club;
import com.clubhub.entity.Records.UserSeason;
import com.clubhub.entity.User;
import com.clubhub.requestBody.AddBulkSeasonRequest;
import com.clubhub.service.ClubService;
import com.clubhub.service.UserSeasonService;
import com.clubhub.service.UserService;
import com.clubhub.validation.GameRecordsValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * A Controller for handling requests to add and update GameRecords
 */
@RestController
@RequestMapping("/api")
public class GameRecordsController {

    private final GameRecordsValidator gameRecordsValidator = new GameRecordsValidator();

    private final ClubService clubService;

    private final UserService userService;

    private final UserSeasonService userSeasonService;

    public GameRecordsController(ClubService clubService, UserService userService, UserSeasonService userSeasonService){
        this.userService = userService;
        this.clubService = clubService;
        this.userSeasonService = userSeasonService;
    }

    /**
     * Add bulk caps for multiple players with minimal detail
     *
     * [{playerId: x, count: y}, {playerId: x, count: y, season: 2022, competition: 1}] Division should be an ID based off the unions they belong to
     */
    @PostMapping("/clubs/{clubId}/statistics/season")
    public ResponseEntity<?> addBulkSeasonCaps(@PathVariable("clubId") Long clubId,
                                               @RequestBody List<AddBulkSeasonRequest> bulkSeasonsRequest,
                                               HttpServletRequest request) {
        System.out.println("POST /clubs/{clubId}/statistics/season");

        // Create Response
        Map<String, Object> response =  new HashMap<>();

        // Check club exists
        Club club = clubService.getById(clubId);
        if (club == null) {
            response.put("error", "Club Not Found");
            return ResponseEntity.status(404).body(response);
        }

        // Check permissions
        User user = userService.getCurrentUser(request.getHeader("Authorization").substring(7));
        if (!(user.getClubMember().getRole().equals("ADMIN") || user.getClubMember().getRole().equals("ROOT"))) {
            response.put("error", "User Is Not Authorized To Perform That Action");
            return ResponseEntity.status(403).body(response);
        }

        // Check if valid
        if (!gameRecordsValidator.validateAddBulkSeasonRequest(bulkSeasonsRequest, response)) {
            return ResponseEntity.status(400).body(response);
        }

        // Add to db
        List<UserSeason> userSeasons = new ArrayList<>();
        for (AddBulkSeasonRequest seasonRequest : bulkSeasonsRequest) {
            User player = userService.getById(seasonRequest.playerId);
            if (player == null) {
                response.put("error", "Player Not Found With ID: " + seasonRequest.playerId);
                return ResponseEntity.status(400).body(response);
            }
            UserSeason userSeason = userSeasonService.createUserSeason(seasonRequest, player);
            userSeasons.add(userSeason);
        }

        // Return response
        return ResponseEntity.status(200).body(userSeasons);
    }

    /**
     * Add a match
     * {opposition: x, date: y, score: z, season: 2022, competition: 1} Can always add more things, and optional things
     */

    /**
     * Get a match
     * returns {opposition: x, date: y, score: z, season: 2022, competition: 1, players: [{playerId x, position: y, minutes: z, points q}, ...]} Can always add more things, and optional things
     * players may be an empty array
     */

    /**
     * Update a match
     */

    /**
     * Delete a match
     */

    /**
     * Add players to a match
     * {match: x, players: [{playerId x, position: y, minutes: z, points q}, ...]}
     */

    /**
     * Update players in a match
     */

    /**
     * Remove a player from a match
     */

    /**
     * View all match and stat history filtered, overview only
     * Filter by, club, competition, grade, between dates, orderBy, position... can always add more things down the line
     * returns [{playerId: 5, count: 12}]
     */

    /**
     * View a further breakdown for individual players. For example when a user clicks see more on a player
     * [{season: 2022, count: 12, matches: null}, {season: 2023, count: 2, matches: [{matchId: 1, opposition: x, date: y, score: z, season: 2022, competition: 1}]}]
     * matches could be altered to be just a list of Ids, however this may then be under-fetching, also would then prompt the user to click on a match to get a full match info break down
     */
}
