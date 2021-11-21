package com.eleks.academy.pharmagator.parsers.csv;

import com.eleks.academy.pharmagator.parsers.annotations.ParsedCollection;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.List;

/**
 * This parser is quite similar to {@link CsvFileParser} except this parser
 * contains processor which can deal with {@link ParsedCollection}
 * For more details about file records processing see {@link MedicineRecordProcessor}
 */
@RequiredArgsConstructor
public final class MedicineRegistryCsvParser extends CsvFileParser {

    private final MedicineRecordProcessor processor;

    /**
     * @param modelClass should have some annotations to tell CsvParser how
     *                   *                   to deserialize objects or else we'll get objects with 'null' properties
     *                   *                   Make sure you're importing annotation from the
     *                   *                   correct package {@link com.univocity.parsers.annotations.Parsed}
     * @return List<T>
     */
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
