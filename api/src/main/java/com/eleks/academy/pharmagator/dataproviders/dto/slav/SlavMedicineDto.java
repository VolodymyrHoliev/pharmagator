package com.eleks.academy.pharmagator.dataproviders.dto.slav;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlavMedicineDto {

	private String id;
	private String name;
	private SlavMecicinePrice price;

}
