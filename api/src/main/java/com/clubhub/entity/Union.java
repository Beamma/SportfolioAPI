package com.clubhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "RugbyUnion")
public class Union {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "union_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "union_clubs",
            joinColumns = @JoinColumn(name = "union_id"),
            inverseJoinColumns = @JoinColumn(name = "club_id"))
    @JsonIgnore
    private List<Club> clubs;
}
