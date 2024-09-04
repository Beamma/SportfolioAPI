package com.clubhub.controller;

import com.clubhub.dto.ClubsDTO;
import com.clubhub.dto.UpdateClubRequestDTO;
import com.clubhub.entity.Club;
import com.clubhub.entity.ClubRequests;
import com.clubhub.entity.User;
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

    private static final List<String> validRequestStatuses = List.of("accepted", "removed", "declined");

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
        Map<String, Object> response = new HashMap<>();

        // Get Club
        Club club = clubService.getById(clubId);
        if (club == null) {
            response.put("clubError", "Club Does Not Exist");
            return ResponseEntity.status(400).body(response);
        }

        // Get Authorization bearer token
        String token = request.getHeader("Authorization").substring(7);

        // Get user information
        User user = userService.getCurrentUser(token);

        // Check that the user doesn't already belong to a club
        if (!clubService.getClubRequestByStatusAndUser("accepted", user).isEmpty() || clubService.getMemberByClubAndUser(club, user) != null) {
            response.put("requestError", "User Already Belongs To A Club");
            return ResponseEntity.status(400).body(response);
        }

        // Check that the user hasn't got any other pending requests
        if (!clubService.getClubRequestByStatusAndUser("pending", user).isEmpty()) {
            response.put("requestError", "User Already Has A Pending Request");
            return ResponseEntity.status(400).body(response);
        }

        // TODO check that the user hasn't already been denied / removed from this club / left the club

        // Create add the entity to the database
        ClubRequests clubRequest = clubService.addClubRequest(club, user);

        // Respond with club information and status
        response.put("club", clubRequest.getClub());
        response.put("status", clubRequest.getStatus());
        response.put("date", clubRequest.getDateResponded());


        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/clubs/{clubId}/request/{requestId}")
    public ResponseEntity<?> updateRequestToJoinClub(@PathVariable("clubId") Long clubId,
                                                     @PathVariable("requestId") Long requestId,
                                                     @RequestBody UpdateClubRequestDTO requestBody,
                                                     HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String requestedStatus = requestBody.status;

        // Get Club
        Club club = clubService.getById(clubId);
        if (club == null) {
            response.put("clubError", "Club Does Not Exist");
            return ResponseEntity.status(400).body(response);
        }

        // Validate requestBody
        if (!validRequestStatuses.contains(requestedStatus)) {
            response.put("statusError", "Status String Does Not Exist");
            return ResponseEntity.status(400).body(response);
        }


        // Get Authorization bearer token
        String token = request.getHeader("Authorization").substring(7);

        // Check users role vs permissions
        if (!userService.userAllowedToUpdateClubRequestStatus(requestedStatus, token)) {
            response.put("permissionError", "You Do Not Have Permission To Perform This Action");
            return ResponseEntity.status(403).body(response);
        }

        // Check if user belongs to club

        // Update request



        return ResponseEntity.status(501).body("Not implemented");
    }


}
