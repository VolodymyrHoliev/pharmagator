package com.eleks.academy.pharmagator.parsers.csv;

import com.eleks.academy.pharmagator.parsers.MedicineRegistryCsvParser;
import com.eleks.academy.pharmagator.parsers.dto.MedicineRegistryRecord;
import com.eleks.academy.pharmagator.parsers.processors.MedicineRecordProcessor;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void parse_ok() throws IOException {
        InputStream inputStream = csvFileResource.getInputStream();

        List<MedicineRegistryRecord> records = subject.parse(inputStream)
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

}