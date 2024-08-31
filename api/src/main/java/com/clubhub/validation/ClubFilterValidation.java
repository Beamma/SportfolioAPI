package com.clubhub.validation;

import com.clubhub.dto.ClubsDTO;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Used for validating the request params for filtering clubs
 */

@NoArgsConstructor
public class ClubFilterValidation {

    private static final String SEARCH_ERROR = "Search Query Contains Invalid Characters";
    private static final String UNION_ERROR = "Union String Contains Invalid Characters";
    private static final String PAGE_SIZE_ERROR = "Page Size Must Be Greater Than 0 and Less Than or Equal to 100";
    private static final String PAGE_ERROR = "Page Number Must Be Between 0 and 100";
    private ClubsDTO clubsDTO;

    public Boolean validateClubFilterData(ClubsDTO clubsDTO) {
        this.clubsDTO = clubsDTO;

        validateSearch();

        validateUnions();

        validatePageSize();

        validatePage();

        return clubsDTO.getError();
    }


    /**
     * Validate that no malicious data is embedded in the search String
     * Sets error in clubDTO
     */
    private void validateSearch() {
        String search = clubsDTO.getSearchQuery();

        if (search == null) {
            return;
        }

        if (!search.matches("[a-zA-Z0-9 ]*")) { // Allow alphanumeric and spaces
            clubsDTO.addError("searchQueryError", SEARCH_ERROR);
        }

    }

    /**
     * Validate that no malicious data is embedded in any of the strings of the unions List<String>
     * Sets error in clubDTO
     */
    private void validateUnions() {
        List<String> unions = clubsDTO.getUnionsQuery();

        if (unions == null) {
            return;
        }

        if (unions.isEmpty()) {
            return;
        }

        for (String union : unions) {
            if (!union.matches("[a-zA-Z0-9 ]*")) { // Allow alphanumeric and spaces
                clubsDTO.addError("unionsQueryError", UNION_ERROR);
            }
        }
    }

    /**
     * Validates that the int page size is > 0 and < 101
     * Sets error in clubDTO
     */
    private void validatePageSize() {
        int pageSize = clubsDTO.getPageSize();

        if (pageSize <= 0 || pageSize > 100) {
            clubsDTO.addError("pageSizeError", PAGE_SIZE_ERROR);
        }
    }

    /**
     * Validates that the int page is > -1 and < 101
     * Sets error in clubDTO
     */
    private void validatePage() {
        int page = clubsDTO.getPage();

        if (page < 0 || page > 100) {
            clubsDTO.addError("pageNumberError", PAGE_ERROR);
        }
    }
}
