package com.eleks.academy.pharmagator.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
