package com.clubhub.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class ClubMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAccepted;

    @OneToOne
    @JoinColumn(name = "accepted_by", nullable = true)
    private User acceptedBy;

    private String role;

    public ClubMembers(Club club, User user, String role, Date dateAccepted) {
        this.club = club;
        this.user = user;
        this.role = role;
        this.dateAccepted = dateAccepted;
    }

    public ClubMembers(Club club, User user, String role, Date dateAccepted, User acceptedBy) {
        this.club = club;
        this.user = user;
        this.role = role;
        this.dateAccepted = dateAccepted;
        this.acceptedBy = acceptedBy;
    }
}
