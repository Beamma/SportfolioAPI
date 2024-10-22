package com.clubhub.controller;

import org.springframework.web.bind.annotation.*;

/**
 * A Controller for handling
 */
@RestController
@RequestMapping("/api")
public class PlayerCapsController {

    /**
     * Add bulk caps for multiple players with minimal detail
     * [{playerId: x, count: y}, {playerId: x, count: y, season: 2022, competition: 1}] Division should be an ID based off the unions they belong to
     */

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
