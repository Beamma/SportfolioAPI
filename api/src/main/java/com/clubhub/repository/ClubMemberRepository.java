package com.clubhub.repository;

import com.clubhub.entity.ClubMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClubMemberRepository extends JpaRepository<ClubMembers, Long> {

    @Query(value = "SELECT DISTINCT CLUB_MEMBERS.* FROM CLUB_MEMBERS WHERE CLUB_ID = ?1 AND USER_ID = ?2", nativeQuery = true)
    ClubMembers findByClubIdAndUserId(Long clubId, Long userId);
}

