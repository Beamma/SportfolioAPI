package com.clubhub.requestBody;

/**
 * Request Object for a bulk request to add via season
 *
 * [{playerId: x, count: y}, {playerId: x, count: y, season: 2022, competition: 1}]
 */
public class AddBulkSeasonRequest {
    public Long playerId;

    public int count;

    public Long season;

    public Long competition;
}
