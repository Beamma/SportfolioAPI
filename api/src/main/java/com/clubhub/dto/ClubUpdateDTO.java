package com.clubhub.dto;

import com.clubhub.entity.Club;
import com.clubhub.entity.ClubMembers;
import com.clubhub.entity.ClubRequests;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ClubUpdateDTO {
    private Map<String, Object> response =  new HashMap<>();

    private String requestedStatus;

    private Club club;

    private String token;

    private ClubMembers clubMember;

    private Long requestId;

    private ClubRequests clubRequests;

    public ClubUpdateDTO () {
    }

    public ClubUpdateDTO (String requestedStatus, Club club, String token, Long requestId) {
        this.requestedStatus = requestedStatus;
        this.club = club;
        this.token = token;
        this.requestId = requestId;
    }

    /**
     * Adds a key value pair to the response object
     * @param key the name of the message
     * @param value the message you want to return in the response, could be an object or a string
     */
    public void updateResponse(String key, Object value) {
        response.put(key, value);
    }
}
