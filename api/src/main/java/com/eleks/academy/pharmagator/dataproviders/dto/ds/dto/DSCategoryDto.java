package com.eleks.academy.pharmagator.dataproviders.dto.ds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DSCategoryDto {

	private String name;
	private String slug;
	private List<DSCategoryDto> children;

}
