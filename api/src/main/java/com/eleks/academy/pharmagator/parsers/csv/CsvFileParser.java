package com.eleks.academy.pharmagator.parsers.csv;

import com.eleks.academy.pharmagator.parsers.ICsvParser;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
public sealed class CsvFileParser implements ICsvParser permits MedicineRegistryCsvParser{

    @Override
    public <T> Stream<T> parse(InputStream inputStream, Class<T> modelType) {
        return parseToModel(inputStream, modelType)
                .stream();
    }

    /**
     * @param modelClass should have some annotations to tell CsvParser how
     *                   to deserialize objects.
     *                   See <a href = https://www.univocity.com/pages/univocity_parsers_tutorial#using-annotations-to-map-your-java-beans>univocity-parsers</a>
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public  <T> List<T> parseToModel(InputStream inputStream, Class<T> modelClass) {
        CsvParserSettings csvParserSettings = getCsvParserSettings();

        BeanListProcessor<?> listProcessor = new BeanListProcessor<>(modelClass);

        csvParserSettings.setProcessor(listProcessor);

        CsvParser parser = getParser(csvParserSettings);

        parser.parse(inputStream);

        return (List<T>) listProcessor.getBeans();
    }





    protected CsvParserSettings getCsvParserSettings() {
        CsvParserSettings parserSettings = new CsvParserSettings();

        //the default value is 4096 which is not suitable for parsing
        //drlz_registry_utf-8.csv
        parserSettings.setMaxCharsPerColumn(8192);

        parserSettings.setDelimiterDetectionEnabled(true);

        parserSettings.setHeaderExtractionEnabled(true);

        return parserSettings;
    }

    protected CsvParser getParser(CsvParserSettings settings) {
        return new CsvParser(settings);
    }

}
