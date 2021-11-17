package com.eleks.academy.pharmagator.parser;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

@Component
public class CsvFileParser implements ICsvParser {

    @Override
    public <T> Stream<T> parse(InputStream inputStream, Class<T> modelType) {
        return parseToModel(inputStream, modelType)
                .stream();
    }

    /**
     *
     * @param modelClass should have some annotations to tell CsvParser how
     *                   to deserialize objects.
     *                   See {@link MedicineRegistryItem} for instance.
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> parseToModel(InputStream inputStream, Class<T> modelClass) {
        CsvParserSettings csvParserSettings = getCsvParserSettings();

        BeanListProcessor<?> listProcessor = new BeanListProcessor<>(modelClass);

        csvParserSettings.setRowProcessor(listProcessor);

        CsvParser parser = getParser(csvParserSettings);

        parser.parse(inputStream);

        return (List<T>) listProcessor.getBeans();
    }

    private CsvParserSettings getCsvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();

        //the default value is 4096 which is not suitable for parsing
        //drlz_registry_utf-8.csv
        parserSettings.setMaxCharsPerColumn(8192);

        parserSettings.setDelimiterDetectionEnabled(true);

        return parserSettings;
    }

    private CsvParser getParser(CsvParserSettings settings) {
        return new CsvParser(settings);
    }

}
