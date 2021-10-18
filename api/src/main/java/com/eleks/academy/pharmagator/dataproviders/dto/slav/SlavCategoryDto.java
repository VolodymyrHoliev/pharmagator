package com.eleks.academy.pharmagator.dataproviders.dto.slav;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlavCategoryDto {

    private int id;
    private String name;
    private List<SlavCategoryDto> children;

}
