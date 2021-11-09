package com.eleks.academy.pharmagator.dataproviders.aptslav.dto;

import lombok.Data;

import java.util.List;

@Data
/**
 * Each API response body consist of a list of data and size of that list
 * T type stands for DTO type we want to retrieve
 */
public class AptslavResponseBody<T> {

    private List<T> data;

    private long count;

}
