package com.clubhub.dto;

import com.clubhub.entity.Club;
import com.clubhub.entity.User;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ClubRequestDTO {
    private Map<String, Object> response =  new HashMap<>();

    private Club club;

    private User user;

    public ClubRequestDTO () {
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
