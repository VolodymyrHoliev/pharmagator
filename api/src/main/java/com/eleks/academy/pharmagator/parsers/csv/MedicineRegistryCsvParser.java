package com.eleks.academy.pharmagator.parsers.csv;

import com.eleks.academy.pharmagator.parsers.MedicineRecordProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
public final class MedicineRegistryCsvParser extends CsvFileParser {

    private final MedicineRecordProcessor processor;

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> parseToModel(InputStream inputStream, Class<T> modelClass) {
        CsvParserSettings csvParserSettings = getCsvParserSettings();

        csvParserSettings.setProcessor(processor);

        CsvParser parser = getParser(csvParserSettings);

        parser.parse(inputStream);

        return (List<T>) processor.getParsedRecords();
    }

}
