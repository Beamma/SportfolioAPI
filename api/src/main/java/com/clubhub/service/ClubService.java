package com.clubhub.service;

import com.clubhub.dto.ClubsDTO;
import com.clubhub.entity.Club;
import com.clubhub.entity.ClubMembers;
import com.clubhub.entity.ClubRequests;
import com.clubhub.entity.User;
import com.clubhub.repository.ClubMemberRepository;
import com.clubhub.repository.ClubRepository;
import com.clubhub.repository.ClubRequestsRepository;
import com.clubhub.specifications.ClubSpecifications;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * Service layer for all things related to the Club entity
 */
@Service
public class ClubService {

    private final ClubRepository clubRepository;

    private final ClubMemberRepository clubMemberRepository;

    private final ClubRequestsRepository clubRequestsRepository;

    public ClubService(ClubRepository clubRepository, ClubMemberRepository clubMemberRepository, ClubRequestsRepository clubRequestsRepository) {
        this.clubMemberRepository = clubMemberRepository;
        this.clubRequestsRepository = clubRequestsRepository;
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
     * list of filtered clubs, order by, pageSize and page
     */
    public void getPaginatedClubsWithUnions(ClubsDTO clubsDTO) {
        List<Long> clubIds = clubsDTO.getFilteredClubs().stream()
                .map(Club::getId)
                .toList();

        // Get orderBy parameter from clubsDTO
        Sort sort = getSortFromOrderBy(clubsDTO.getOrderBy());
        System.out.println(clubIds);
        Pageable pageable = PageRequest.of(clubsDTO.getPage(), clubsDTO.getPageSize(), sort);
        clubsDTO.setClubsPaginated(clubRepository.findByIdIn(clubIds, pageable));
    }

    /**
     * Maps the orderBy field to a Sort object
     * @param orderBy The orderBy field from ClubsDTO
     * @return Sort object corresponding to the orderBy field
     */
    private Sort getSortFromOrderBy(String orderBy) {
        if (orderBy == null) {
            return Sort.unsorted(); // Default to no sorting
        }

        return switch (orderBy) { // WARNING when altering here, need to reflect in validator
            case "ID_ASC" -> Sort.by(Sort.Order.asc("id")); // Adjust "clubId" to match your field name
            case "NAME_ASC" -> Sort.by(Sort.Order.asc("name")); // Adjust "name" to match your field name
            case "NAME_DESC" -> Sort.by(Sort.Order.desc("name")); // Adjust "name" to match your field name
            default -> throw new IllegalArgumentException("Invalid orderBy value: " + orderBy);
        };
    }

    /**
     * Gets the club by its ID
     * @param id, the clubs ID
     * @return the club entity
     */
    public Club getById(Long id) {
        return clubRepository.findById(id).orElse(null);
    }

    /**
     * Given a user and club, create a request to join the club
     * @param club the club the user is requesting to join
     * @param user the user who is requesting to join a club
     * @return the request entity created in the database
     */
    public ClubRequests addClubRequest(Club club, User user) {

        ClubRequests clubRequest = new ClubRequests(club, user, "pending", new Date());

        return clubRequestsRepository.save(clubRequest);
    }

    /**
     * Get all requests by user and a given status
     * @param status of the request
     * @param user who requested
     * @return any club requests that match
     */
    public List<ClubRequests> getClubRequestByStatusAndUser(String status, User user) {
        return clubRequestsRepository.findByStatusAndUser(status, user.getId());
    }

    /**
     * Given a club and user, return a club membership entity
     * @return return a club membership entity that matches the given club and user
     */
    public ClubMembers getMemberByClubAndUser(Club club, User user) {
        return clubMemberRepository.findByClubIdAndUserId(club.getId(), user.getId());
    }

    public ClubMembers acceptRequest(Long requestId) {

        // Attempt to update the request
        ClubRequests request = updateClubRequest(requestId, "accepted");
        if (request == null) {
            return null;
        }

        if (clubMemberRepository.findByClubIdAndUserId(request.getClub().getId(), request.getUser().getId()) != null) {
            return null;
        }

        // If successful, then create a new club membership identity
        ClubMembers clubMembers = new ClubMembers(request.getClub(), request.getUser(), "MEMBER", new Date());
        clubMembers = clubMemberRepository.save(clubMembers);


        return clubMembers;
    }


    public ClubRequests updateClubRequest(Long requestId, String status) {


        Optional<ClubRequests> optionalRequest = clubRequestsRepository.findById(requestId);


        if (optionalRequest.isEmpty()) {
            return null;
        }

        ClubRequests request = optionalRequest.get();
        request.setStatus(status);
        request.setDateResponded(new Date());
        return clubRequestsRepository.save(request);
    }
}
