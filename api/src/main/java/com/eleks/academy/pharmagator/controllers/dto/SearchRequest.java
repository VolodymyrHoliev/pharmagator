package com.eleks.academy.pharmagator.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String searchQuery;

    private String sortOrder;

    public String getSortBy(){
        return sortOrder.split(":")[0];
    }

    public Sort.Direction getSortDirection(){
        String directionString = sortOrder.split(":")[1];

        return Sort.Direction.fromString(directionString);
    }
}
