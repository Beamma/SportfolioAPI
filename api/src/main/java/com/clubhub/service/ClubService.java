package com.clubhub.service;

import com.clubhub.dto.ClubRequestDTO;
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

    //Error for messages validation of club requests
    private static final String CLUB_ERROR = "Club Does Not Exist";
    private static final String BELONGS_TO_CLUB_ERROR = "User Already Belongs To A Club";
    private static final String ALREADY_PENDING_REQUEST_ERROR = "User Already Has A Pending Request";
    private static final String USER_ALREADY_DENIED_ERROR = "User Has Already Been Denied From This Club";
    private static final String USER_ALREADY_LEFT_ERROR = "User Has Already Left From This Club";
    private static final String USER_ALREADY_REMOVED_ERROR = "User Has Already Left From This Club";

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
    private List<ClubRequests> getClubRequestByStatusAndUser(String status, User user) {
        return clubRequestsRepository.findByStatusAndUser(status, user.getId());
    }

    /**
     * Given a club and user, return a club membership entity
     * @return return a club membership entity that matches the given club and user
     */
    private ClubMembers getMemberByClubAndUser(Club club, User user) {
        return clubMemberRepository.findByClubIdAndUserId(club.getId(), user.getId());
    }

    /**
     * Accepts a request to join a club. Updates the request status, and adds the user to the ClubMembers table
     * @param requestId the ClubRequests id
     * @return the new ClubMembers
     */
    public ClubMembers acceptRequest(Long requestId) {

        // Attempt to update the request
        ClubRequests request = updateClubRequest(requestId, "accepted");
        if (request == null) {
            return null;
        }

        if (!request.getStatus().equals("pending")) {
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

    /**
     * Denies a request to join a club. Updates the request status, and removes the user to the ClubMembers table
     * @param requestId the ClubRequests id
     * @return the updated ClubRequests
     */
    public ClubRequests denyRequest(Long requestId) {
        // Attempt to update the request
        ClubRequests request = updateClubRequest(requestId, "removed");
        if (request == null) {
            return null;
        }

        if (!request.getStatus().equals("accepted")) {
            return null;
        }

        ClubMembers clubMember = clubMemberRepository.findByClubIdAndUserId(request.getClub().getId(), request.getUser().getId());
        if (clubMember == null) {
            return null;
        }

        clubMemberRepository.delete(clubMember);


        return request;
    }

    /**
     * Updates a club request, with the given status
     * @param requestId the ClubRequests id
     * @param status the status you wish to set the ClubRequest too
     * @return the updated ClubRequests
     */
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

    /**
     * Check for any requests that match the given status user and club
     * @param status the status of the request to check for
     * @param user the user to check for
     * @param club the club to check for
     * @return a list of all club requests that match the above params
     */
    private List<ClubRequests> getClubRequestByStatusUserAndClub(String status, User user, Club club) {
        return clubRequestsRepository.findByStatusUserAndClub(status, user.getId(), club.getId());
    }


    /**
     * Validates a request to join a club, adds error messages to DTOs response if request is not valid
     * @param clubRequestDTO a DTO to carry all the information about a request to join a club
     * @return true if the request is valid, false if the request is invalid
     */
    public boolean clubRequestIsValid(ClubRequestDTO clubRequestDTO) {

        // Check if club is null
        if (clubRequestDTO.getClub() == null) {
            clubRequestDTO.updateResponse("clubError", CLUB_ERROR);
            return false;
        }

        // Check that the user doesn't already belong to a club
        if (!getClubRequestByStatusAndUser("accepted", clubRequestDTO.getUser()).isEmpty() || getMemberByClubAndUser(clubRequestDTO.getClub(), clubRequestDTO.getUser()) != null) {
            clubRequestDTO.updateResponse("requestError", BELONGS_TO_CLUB_ERROR);
            return false;
        }

        // Check that the user hasn't got any other pending requests
        if (!getClubRequestByStatusAndUser("pending", clubRequestDTO.getUser()).isEmpty()) {
            clubRequestDTO.updateResponse("requestError", ALREADY_PENDING_REQUEST_ERROR);
            return false;
        }

        // check that the user hasn't already been denied from this club
        if (!getClubRequestByStatusUserAndClub("declined", clubRequestDTO.getUser(), clubRequestDTO.getClub()).isEmpty()) {
            clubRequestDTO.updateResponse("requestError", USER_ALREADY_DENIED_ERROR);
            return false;
        }

        // check that the user hasn't already been removed from this club
        if (!getClubRequestByStatusUserAndClub("removed", clubRequestDTO.getUser(), clubRequestDTO.getClub()).isEmpty()) {
            clubRequestDTO.updateResponse("requestError", USER_ALREADY_REMOVED_ERROR);
            return false;
        }

        // check that the user hasn't already left from this club
        if (!getClubRequestByStatusUserAndClub("quit", clubRequestDTO.getUser(), clubRequestDTO.getClub()).isEmpty()) {
            clubRequestDTO.updateResponse("requestError", USER_ALREADY_LEFT_ERROR);
            return false;
        }

        return true;
    }

    /**
     * Given the request has been validated, then taking the club and user from the DTO, add a request in the database
     * update the response with the required fields
     * @param clubRequestDTO a DTO to carry all the required information
     */
    public void handleValidRequest(ClubRequestDTO clubRequestDTO) {
        // Create add the entity to the database
        ClubRequests clubRequest = addClubRequest(clubRequestDTO.getClub(), clubRequestDTO.getUser());

        // Respond with club information and status
        clubRequestDTO.updateResponse("requestId", clubRequest.getId());
        clubRequestDTO.updateResponse("club", clubRequest.getClub());
        clubRequestDTO.updateResponse("status", clubRequest.getStatus());
        clubRequestDTO.updateResponse("date", clubRequest.getDateResponded());

    }
}
