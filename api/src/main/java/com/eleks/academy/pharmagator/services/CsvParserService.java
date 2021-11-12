package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;

import java.io.InputStream;
import java.util.List;

public interface CsvParserService {

    List<MedicineDto> parse(InputStream inputStream);

}
