package com.clubhub.entity;

import jakarta.persistence.*;

import java.util.Date;

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
    private Date date;
}
