package com.eleks.academy.pharmagator.dataproviders.dto.general;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterRequest {
	private Long page;
	private Long per;
}
