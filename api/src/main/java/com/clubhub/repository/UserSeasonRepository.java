package com.clubhub.repository;

import com.clubhub.entity.Records.UserSeason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSeasonRepository extends JpaRepository<UserSeason, Long> {
}
