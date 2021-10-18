package com.eleks.academy.pharmagator.dataproviders.ds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DSMedicineDto {

	private String id;
	private String name;
	@JsonProperty("ціна")
	private BigDecimal price;
	private String manufacturer;

}
