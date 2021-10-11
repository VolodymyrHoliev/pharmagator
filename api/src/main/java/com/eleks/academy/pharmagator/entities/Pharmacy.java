package com.eleks.academy.pharmagator.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "pharmacies")
public class Pharmacy {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String medicineLinkTemplate;
}
