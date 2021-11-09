package com.eleks.academy.pharmagator.dataproviders.aptslav.dto;
/**
 * @author Marharyta Levtsun
 * @see <a href = "https://github.com/ritalinch"</a>
 */

import lombok.Data;

import java.util.List;

@Data
public class AptslavCategoryDto {

    private int id;

    private String name;

    private List<AptslavCategoryDto> children;

}
