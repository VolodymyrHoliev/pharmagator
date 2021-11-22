package com.eleks.academy.pharmagator.parsers.csv;

import com.eleks.academy.pharmagator.parsers.CsvFileParser;
import com.eleks.academy.pharmagator.parsers.dto.MedicineRegistryRecord;
import com.eleks.academy.pharmagator.parsers.exceptions.UnsupportedModelTypeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CsvFileParserIT {

    private final CsvFileParser subject = new CsvFileParser();

    @Value("classpath:csv/test-file.csv")
    private Resource csvFileResource;

    @Value("classpath:csv/test-file-2.csv")
    private Resource anotherResource;

    @Test
    void contextLoads() {

    }

    @Test
    void parse_ok() throws IOException {
        InputStream inputStream = anotherResource.getInputStream();

        MedicineDtoLight dtoLight = MedicineDtoLight.builder()
                .id("416003B28674EC50C225863100349916")
                .title("БЕТАК")
                .build();

        MedicineDtoLight anotherDtoLight = MedicineDtoLight.builder()
                .id("17215E70E4F26B6DC225878300453914")
                .title("БЕТАДЕРМ®")
                .build();

        Stream<MedicineDtoLight> dtoLightStream = subject.parse(inputStream, MedicineDtoLight.class);

        List<MedicineDtoLight> dtoLightList = dtoLightStream.collect(Collectors.toList());

        System.out.println(dtoLightList);

        assertEquals(2 , dtoLightList.size());

        assertTrue(dtoLightList.contains(dtoLight));

        assertTrue(dtoLightList.contains(anotherDtoLight));
    }


    @Test
    void parse_unsupportedModelType_UnsupportedModelException() throws IOException {
        InputStream inputStream = csvFileResource.getInputStream();

        String errorMessage = assertThrows(UnsupportedModelTypeException.class,
                () -> subject.parse(inputStream, MedicineRegistryRecord.class))
                .getMessage();

        assertEquals("Can't map records from .csv to model with @ParsedCollection annotated fields\n" +
                "Consider use another parser or remove @ParsedCollection annotation", errorMessage);
    }
}