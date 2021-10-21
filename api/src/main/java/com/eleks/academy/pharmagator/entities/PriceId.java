package com.eleks.academy.pharmagator.entities;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PriceId implements Serializable {

    private long pharmacyId;

    private long medicineId;
}
