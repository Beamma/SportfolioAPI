package com.clubhub.dto;

import com.clubhub.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ClubsDTO {

    private List<Club> filteredClubs;

    private Page<Club> clubsPaginated;

    private List<String> unionsQuery;
    private String searchQuery;
    private int pageSize;
    private int page;
    private boolean error;
    public Map<String, Object> errors;


    public void addError (String errorName, String error) {
        this.error = true;

        errors.put(errorName, error);
    }

    public boolean getError() {
        return this.error;
    }
}
