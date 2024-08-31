package com.clubhub.dto;

import com.clubhub.entity.Club;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ClubsDTO {

    private List<Club> filteredClubs;

    private Page<Club> clubsPaginated;

    private List<String> unionsQuery;
    private String searchQuery;
    private int pageSize;
    private int page;


}
