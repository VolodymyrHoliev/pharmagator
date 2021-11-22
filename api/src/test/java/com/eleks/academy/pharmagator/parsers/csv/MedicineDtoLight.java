package com.eleks.academy.pharmagator.parsers.csv;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineDtoLight {

    @Parsed(field = "ID")
    private String id;

    @Parsed(field = "Торгівельне найменування")
    private String title;
}
