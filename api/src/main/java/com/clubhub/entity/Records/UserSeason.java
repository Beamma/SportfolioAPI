package com.clubhub.entity.Records;

import com.clubhub.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Season Entity
 * Stores records for each player for each season
 */
@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class UserSeason {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userSeasonId;

    @Column(nullable = false)
    private int count;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User player;

    @Column
    private Long season;

    @Column
    private Long competition;


    public UserSeason(int count, User player, Long season, Long competition) {
        this. count = count;
        this.player = player;
        this.season = season;
        this.competition = competition;
    }
}
