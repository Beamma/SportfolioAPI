package com.clubhub.controller;

import com.clubhub.dto.ClubsDTO;
import com.clubhub.service.ClubService;
import com.clubhub.validation.ClubFilterValidation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClubController {

    private final ClubFilterValidation clubFilterValidation = new ClubFilterValidation();

    private final ClubService clubService;
    public ClubController(ClubService clubService){
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

}
