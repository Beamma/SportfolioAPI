package com.clubhub.entity.Records;

import com.clubhub.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private User playerId;

    @Column(nullable = false)
    private int season;

    @Column(nullable = false)
    private int competition;
}
