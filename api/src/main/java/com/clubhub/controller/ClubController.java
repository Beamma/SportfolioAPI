package com.clubhub.controller;

import com.clubhub.dto.ClubRequestDTO;
import com.clubhub.dto.ClubUpdateDTO;
import com.clubhub.dto.ClubsDTO;
import com.clubhub.dto.UpdateClubRequestDTO;
import com.clubhub.entity.Club;
import com.clubhub.entity.ClubMembers;
import com.clubhub.entity.ClubRequests;
import com.clubhub.service.ClubService;
import com.clubhub.service.UserService;
import com.clubhub.validation.ClubFilterValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClubController {

    private final ClubFilterValidation clubFilterValidation = new ClubFilterValidation();

    private final ClubService clubService;

    private final UserService userService;

    public ClubController(ClubService clubService, UserService userService){
        this.userService = userService;
        this.clubService = clubService;
    }

    @GetMapping("/clubs")
    public ResponseEntity<?> getFilteredClubs(@RequestParam(value = "search", required = false) String search,
                                              @RequestParam(value = "unions", required = false) List<String> unions,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                              @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                              @RequestParam(value = "orderBy", required = false, defaultValue = "ID_ASC") String orderBy) {
        System.out.println("GET /clubs");

        // Create DTO
        ClubsDTO clubsDTO = new ClubsDTO(null, null, unions, search, pageSize, page, false, new HashMap<>(), orderBy);

        // Validate Request
        if (clubFilterValidation.validateClubFilterData(clubsDTO)) {
            return ResponseEntity.status(400).body(clubsDTO.getErrors());
        }

        // Get all clubs that match filter
        clubService.getListFilteredClubs(clubsDTO);

        //  Get all clubs paginated, with unions
        clubService.getPaginatedClubsWithUnions(clubsDTO);

        // Return Response
        return ResponseEntity.ok(clubsDTO.getClubsPaginated());
    }

    @PostMapping("/clubs/{clubId}/request")
    public ResponseEntity<?> requestToJoinClub(@PathVariable("clubId") Long clubId, HttpServletRequest request) {
        System.out.println("POST /clubs/{id}/request");

        // Construct DTO
        ClubRequestDTO clubRequestDTO = new ClubRequestDTO();

        // Get Club
        clubRequestDTO.setClub(clubService.getById(clubId));

        // Get Current User
        String token = request.getHeader("Authorization").substring(7);
        clubRequestDTO.setUser(userService.getCurrentUser(token));

        // Validate Request
        if (!clubService.clubRequestIsValid(clubRequestDTO)) {
            return ResponseEntity.status(400).body(clubRequestDTO.getResponse());
        }

        // Must be a valid request, so add it to database and add the appropriate things to the response
        clubService.handleValidRequest(clubRequestDTO);

        return ResponseEntity.status(200).body(clubRequestDTO.getResponse());
    }

    @PutMapping("/clubs/{clubId}/request/{requestId}")
    public ResponseEntity<?> updateRequestToJoinClub(@PathVariable("clubId") Long clubId,
                                                     @PathVariable("requestId") Long requestId,
                                                     @RequestBody UpdateClubRequestDTO requestBody,
                                                     HttpServletRequest request) {
        System.out.println("PUT /clubs/{id}/request/{id}");

        ClubUpdateDTO clubUpdateDTO = new ClubUpdateDTO(requestBody.status, clubService.getById(clubId), request.getHeader("Authorization").substring(7), requestId);

        if (!clubService.clubUpdateIsValid(clubUpdateDTO)) {
            return ResponseEntity.status(400).body(clubUpdateDTO.getResponse());
        }

        // Check users role vs permissions
        if (!userService.userAllowedToUpdateClubRequestStatus(clubUpdateDTO)) { // Change to use ClubMember Role
            clubUpdateDTO.updateResponse("permissionError", "You Do Not Have Permission To Perform This Action");
            return ResponseEntity.status(403).body(clubUpdateDTO.getResponse());
        }

        // Update the club request
        if (!clubService.handleUpdateRequest(clubUpdateDTO)) {
            return ResponseEntity.status(400).body(clubUpdateDTO.getResponse());
        }

        // If successful update, return successful response
        return ResponseEntity.status(200).body(clubUpdateDTO.getResponse());
    }
}
