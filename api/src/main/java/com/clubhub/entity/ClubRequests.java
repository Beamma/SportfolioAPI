package com.clubhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class ClubRequests {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateResponded;

    public ClubRequests(Club club, User user, String status, Date dateResponded) {
        this.club = club;
        this.user = user;
        this.status = status;
        this.dateResponded = dateResponded;
    }
}
