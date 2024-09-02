package com.clubhub.repository;

import com.clubhub.entity.ClubMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMembers, Long> {

}

