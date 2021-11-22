package com.eleks.academy.pharmagator.parsers.processors;

import com.eleks.academy.pharmagator.parsers.annotations.ParsedCollection;
import com.eleks.academy.pharmagator.parsers.dto.MedicineRegistryRecord;
import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.helpers.MethodFilter;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.core.BeanConversionProcessor;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.common.record.Record;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Our drlz_registry_utf-8.csv contains a lot of similar columns which we want to save in a list
 * Therefore, we need a custom implementation of BeanConversionProcessor
 * For more details take a look at {@link MedicineRegistryRecord}
 */
@Getter
@Component
public class MedicineRecordProcessor extends BeanConversionProcessor<MedicineRegistryRecord> implements Processor<ParsingContext> {

    private List<MedicineRegistryRecord> parsedRecords;

    public MedicineRecordProcessor() {
        super(MedicineRegistryRecord.class, MethodFilter.ONLY_GETTERS);
    }

    @Override
    public void processStarted(ParsingContext context) {
        parsedRecords = new ArrayList<>();
    }

    @SneakyThrows
    @Override
    public void rowProcessed(String[] row, ParsingContext context) {
        MedicineRegistryRecord instance = new MedicineRegistryRecord();

        Record record = context.toRecord(row);

        Class<MedicineRegistryRecord> modelClass = getBeanClass();

        Field[] declaredFields = modelClass.getDeclaredFields();

        for (Field field : declaredFields) {

            field.setAccessible(true);

            if (field.isAnnotationPresent(Parsed.class) && field.getType().equals(String.class)) {
                Parsed annotation = field.getAnnotation(Parsed.class);

                String headerName = annotation.field()[0];

                String recordValue = record.getString(headerName);

                field.set(instance, recordValue);
            }

            if (field.isAnnotationPresent(ParsedCollection.class)) {
                ParsedCollection annotation = field.getAnnotation(ParsedCollection.class);

                String[] headersNames = annotation.fields();

                ArrayList<String> values = new ArrayList<>();

                for (String headerName : headersNames) {
                    String recordValue = record.getString(headerName);

                    if (recordValue != null) {
                        values.add(recordValue);
                    }
                }
                field.set(instance, values);
            }
        }

        parsedRecords.add(instance);

    }

    @Override
    public void processEnded(ParsingContext context) {

    }

}
