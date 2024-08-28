package com.clubhub.entity;

import jakarta.persistence.*;

/**
 * A user entity that stores all data
 * about the users that are registered
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

}