//package com.clubhub.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//
//@Data
//@NoArgsConstructor
//@Entity
//@AllArgsConstructor
//@Table(name = "Match")
//public class Match {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "match_id")
//    private Long id;
//
//    private MatchTeam homeTeam;
//
//    private MatchTeam awayTeam;
//
//    @Column(nullable = false)
//    private Date matchDate;
//
//    @Column(nullable = false)
//    private int homeTeamScore;
//
//    @Column(nullable = false)
//    private int awayTeamScore;
//
//    @Column(nullable = false)
//    private int season;
//
//    private Competition competition;
//
//    @Column(nullable = false)
//    private Date createdDate = new Date();
//
//
//
//
//
//
//}
