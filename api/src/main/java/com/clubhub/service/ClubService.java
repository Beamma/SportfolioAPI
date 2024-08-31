package com.clubhub.service;

import com.clubhub.dto.ClubsDTO;
import com.clubhub.entity.Club;
import com.clubhub.repository.ClubRepository;
import com.clubhub.specifications.ClubSpecifications;
import org.springframework.data.domain.Page;
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
     * Get all clubs in the Club entity
     * @param clubsDto a dto to carry data throughout the classes
     */
    public void getAllClubs(ClubsDTO clubsDto) {
        clubsDto.setAllClubs(clubRepository.findAll());
    }

    /**
     * Applies the filter and get a Page of clubs
     * @param clubsDTO a DTO, that holds information on
     * searchQuery, unionsQuery, page, and pageSize
     */
    public void getFilteredClubs(ClubsDTO clubsDTO) {
        Specification<Club> spec = ClubSpecifications.filterClubs(clubsDTO.getSearchQuery(), clubsDTO.getUnionsQuery());
        Pageable pageable = PageRequest.of(clubsDTO.getPage(), clubsDTO.getPageSize());
        clubsDTO.setClubsPage(clubRepository.findAll(spec, pageable));
    }
}
