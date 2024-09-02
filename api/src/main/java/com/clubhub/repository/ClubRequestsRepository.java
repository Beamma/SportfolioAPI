package com.clubhub.repository;

import com.clubhub.entity.ClubRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubRequestsRepository extends JpaRepository<ClubRequests, Long> {

    @Query(value = "SELECT * FROM CLUB_REQUESTS WHERE USER_ID = ?2 AND STATUS = ?1", nativeQuery = true)
    List<ClubRequests> findByStatusAndUser(String status, Long userId);
}
