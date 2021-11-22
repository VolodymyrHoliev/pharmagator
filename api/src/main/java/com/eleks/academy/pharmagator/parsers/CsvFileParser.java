package com.eleks.academy.pharmagator.parsers;

import com.eleks.academy.pharmagator.parsers.annotations.ParsedCollection;
import com.eleks.academy.pharmagator.parsers.exceptions.UnsupportedModelTypeException;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import lombok.AllArgsConstructor;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * This parser wraps {@link CsvParser} and contains default
 * {@link CsvParserSettings} and {@link com.univocity.parsers.common.processor.core.Processor}
 * objects.
 */
@AllArgsConstructor
public class CsvFileParser implements ICsvParser {

    /**
     * @param modelType should have some annotations to tell CsvParser how
     *                  to deserialize objects or else we'll get objects with 'null' properties
     *                  Make sure you're importing annotation from the
     *                  correct package {@link com.univocity.parsers.annotations.Parsed}
     *                  <p>
     *                  See <a href = https://www.univocity.com/pages/univocity_parsers_tutorial#using-annotations-to-map-your-java-beans>univocity-parsers</a>
     * @return List<T>
     * @throws UnsupportedModelTypeException in case your model contains fields, annotated with {@link ParsedCollection}
     */

    @Override
    public <T> Stream<T> parse(InputStream inputStream, Class<T> modelType) throws UnsupportedModelTypeException {
        return parseToModel(inputStream, modelType)
                .stream();
    }

    /**
     * @param modelClass should have some annotations to tell CsvParser how
     *                   *                   to deserialize objects or else we'll get objects with 'null' properties
     *                   *                   Make sure you're importing annotation from the
     *                   *                   correct package {@link com.univocity.parsers.annotations.Parsed}
     * @return List<T>, keep in mind, that properties of T may be nullable
     * @throws UnsupportedModelTypeException in case your model contains fields, annotated with {@link ParsedCollection}
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> parseToModel(InputStream inputStream, Class<T> modelClass) throws UnsupportedModelTypeException {
        checkModelClass(modelClass);

        CsvParserSettings csvParserSettings = getCsvParserSettings();

        BeanListProcessor<?> listProcessor = new BeanListProcessor<>(modelClass);

        csvParserSettings.setProcessor(listProcessor);

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

        parserSettings.setHeaderExtractionEnabled(true);

        return parserSettings;
    }

    private CsvParser getParser(CsvParserSettings settings) {
        return new CsvParser(settings);
    }

    /**
     * Checks whether modelClass contains field, annotated with {@link ParsedCollection}
     * This parser use one of the default univocity bean processor which knows nothing
     * about {@link ParsedCollection} and how to handle it.
     *
     * @throws UnsupportedModelTypeException if at least one field of modelClass
     * contains {@link ParsedCollection} annotation
     */
    private void checkModelClass(Class<?> modelClass) throws UnsupportedModelTypeException{
        Arrays.stream(modelClass.getDeclaredFields())
                .toList()
                .forEach(field -> {
                    if (field.isAnnotationPresent(ParsedCollection.class)) {
                        throw new UnsupportedModelTypeException();
                    }
                });
    }

}
