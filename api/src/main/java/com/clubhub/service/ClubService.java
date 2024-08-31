package com.clubhub.service;

import com.clubhub.dto.ClubsDTO;
import com.clubhub.entity.Club;
import com.clubhub.repository.ClubRepository;
import com.clubhub.specifications.ClubSpecifications;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service layer for all things related to the Club entity
 */
@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }


    /**
     * Applies the filter and get a List of clubs
     * @param clubsDTO a DTO, that holds information on
     * searchQuery, unionsQuery
     */
    public void getListFilteredClubs(ClubsDTO clubsDTO) {
        Specification<Club> spec = ClubSpecifications.filterClubs(clubsDTO.getSearchQuery(), clubsDTO.getUnionsQuery());
        clubsDTO.setFilteredClubs(clubRepository.findAll(spec));
    }

    /**
     * Used to get a Paginated list of clubs that have already been filtered
     * @param clubsDTO a DTO, that holds information on
     * list of filtered clubs, pageSize and page
     */
    public void getPaginatedClubsWithUnions(ClubsDTO clubsDTO) {
        List<Long> clubIds = clubsDTO.getFilteredClubs().stream()
                .map(Club::getId)
                .toList();
        Pageable pageable = PageRequest.of(clubsDTO.getPage(), clubsDTO.getPageSize());
        clubsDTO.setClubsPaginated(clubRepository.findByIdIn(clubIds, pageable));
    }
}
