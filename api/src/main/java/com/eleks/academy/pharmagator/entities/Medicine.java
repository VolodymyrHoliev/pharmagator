package com.eleks.academy.pharmagator.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "medicines")
public class Medicine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
}
