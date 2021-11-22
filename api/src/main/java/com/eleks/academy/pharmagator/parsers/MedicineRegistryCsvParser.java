package com.eleks.academy.pharmagator.parsers;

import com.eleks.academy.pharmagator.parsers.annotations.ParsedCollection;
import com.eleks.academy.pharmagator.parsers.dto.MedicineRegistryRecord;
import com.eleks.academy.pharmagator.parsers.processors.MedicineRecordProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.util.stream.Stream;

/**
 * This parser is quite similar to {@link CsvFileParser} except this parser
 * contains {@link MedicineRecordProcessor} instance and implements {@link ITypedCsvParser} interface
 * Thanks to the processor this parser can deal with {@link ParsedCollection} annotation
 * For more details about file records processing see {@link MedicineRecordProcessor}
 */
@RequiredArgsConstructor
public final class MedicineRegistryCsvParser implements ITypedCsvParser<MedicineRegistryRecord> {

    private final MedicineRecordProcessor processor;

    @Override
    public Stream<MedicineRegistryRecord> parse(InputStream inputStream) {
        CsvParserSettings csvParserSettings = getCsvParserSettings();

        csvParserSettings.setProcessor(processor);

        CsvParser parser = getParser(csvParserSettings);

        parser.parse(inputStream);

        return processor.getParsedRecords().stream();
    }

    private CsvParserSettings getCsvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();

        //the default value is 4096 which is not suitable for parsing
        //drlz_registry_utf-8.csv
        parserSettings.setMaxCharsPerColumn(8192);

        parserSettings.setDelimiterDetectionEnabled(true);

        parserSettings.setHeaderExtractionEnabled(true);

        return parserSettings;
    }

    private CsvParser getParser(CsvParserSettings settings) {
        return new CsvParser(settings);
    }
}
