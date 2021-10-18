package com.eleks.academy.pharmagator.dataproviders.dto.general;

import com.eleks.academy.pharmagator.dataproviders.dto.ds.DSMedicineDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinesResponse {

	private Long total;
	private List<DSMedicineDto> products;

}
