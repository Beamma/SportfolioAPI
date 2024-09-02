package com.clubhub.entity;

import jakarta.persistence.*;

import java.util.Date;

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

    private String respondedBy;

    private String role;
}
