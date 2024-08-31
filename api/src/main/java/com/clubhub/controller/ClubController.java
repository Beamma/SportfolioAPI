package com.clubhub.controller;

import com.clubhub.dto.ClubsDTO;
import com.clubhub.service.ClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClubController {

    private final ClubService clubService;
    public ClubController(ClubService clubService){
        this.clubService = clubService;
    }

    @GetMapping("/clubs")
    public ResponseEntity<?> getFilteredClubs(@RequestParam(value = "search", required = false) String search,
                                              @RequestParam(value = "unions", required = false) List<String> unions,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                              @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
        System.out.println("GET /clubs");

        // Create DTO
        ClubsDTO clubsDTO = new ClubsDTO(null, null, unions, search, pageSize, page);

        // Validate Request

        // Get all clubs that match filter
        clubService.getFilteredClubs(clubsDTO);

        // Return Response

        return ResponseEntity.ok(clubsDTO.getClubsPage().getContent());
    }

}
