package com.eleks.academy.pharmagator.parsers.csv;

import com.eleks.academy.pharmagator.parsers.MedicineRecordProcessor;
import com.eleks.academy.pharmagator.parsers.MedicineRegistryRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class MedicineRegistryCsvParserIT {

    @Value("classpath:csv/test-file.csv")
    private Resource csvFileResource;

    @Value("classpath:csv/test-file-2.csv")
    private Resource anotherResource;

    private MedicineRegistryCsvParser subject;

    @Autowired
    void setComponents(MedicineRecordProcessor processor) {
        subject = new MedicineRegistryCsvParser(processor);
    }

    @Test
    void parse_ok_medicineRegistryModel() throws IOException {
        InputStream inputStream = csvFileResource.getInputStream();

        List<MedicineRegistryRecord> records = subject.parse(inputStream, MedicineRegistryRecord.class)
                .collect(Collectors.toList());

        assertEquals(5, records.size());

        records.forEach(medicineRegistryRecord -> {
            assertNotNull(medicineRegistryRecord.getNumberOfManufacturers());

            assertNotNull(medicineRegistryRecord.getTitle());

            assertNotNull(medicineRegistryRecord.getRegistrationId());

            assertNotNull(medicineRegistryRecord.getManufacturersCountries());

            assertNotNull(medicineRegistryRecord.getManufacturers());

            assertNotNull(medicineRegistryRecord.getLicensedAt());

            assertNotNull(medicineRegistryRecord.getDosageForm());

            assertNotNull(medicineRegistryRecord.getApplicantName());

            assertNotNull(medicineRegistryRecord.getCompositionOfActiveSubstances());

            assertNotNull(medicineRegistryRecord.getApplicantCountry());
        });
    }

    @Test
    void parse_ok_medicineDtoLight() throws IOException {
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

}