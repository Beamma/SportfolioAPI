package com.clubhub.specifications;

import com.clubhub.entity.Club;
import com.clubhub.entity.Union;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class ClubSpecifications {

    public static Specification<Club> filterClubs(String search, List<String> unions) {
        return (Root<Club> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + search + "%"));
            }

            // Add union filter
            if (unions != null && !unions.isEmpty()) {
                Join<Club, Union> unionJoin = root.join("unions");
                predicates.add(unionJoin.get("name").in(unions));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}