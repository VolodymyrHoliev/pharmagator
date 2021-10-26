package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.AbstractDataIT;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.projections.MedicineDto;
import com.eleks.academy.pharmagator.projections.PharmacyDto;
import com.eleks.academy.pharmagator.projections.PriceDto;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PharmacyRepositoryIT extends AbstractDataIT {

    private PharmacyRepository subject;
    private DatabaseDataSourceConnection dataSourceConnection;


    @Autowired
    public void setSubject(final PharmacyRepository pharmacyRepository, DataSource dataSource) throws SQLException {
        this.subject = pharmacyRepository;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
    }

    @Test
    public void findAllPharmacies_pharmacyDto_ok() throws Exception {
        try {

            refreshDatabase();

            List<PharmacyDto> allPharmacies = subject.findAllPharmacies(PharmacyDto.class);

            List<String> namesList = allPharmacies.stream()
                    .map(PharmacyDto::getName)
                    .collect(Collectors.toList());

            assertTrue(namesList.contains("Pharmacy_1"));

            assertTrue(namesList.contains("Pharmacy_2"));

            assertTrue(namesList.contains("Pharmacy_3"));
        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findAllPharmacies_pharmacy_ok() throws Exception {
        try {

            refreshDatabase();

            List<Pharmacy> allPharmacies = subject.findAllPharmacies(Pharmacy.class);

            List<String> namesList = allPharmacies.stream()
                    .map(Pharmacy::getName)
                    .collect(Collectors.toList());

            assertTrue(namesList.contains("Pharmacy_1"));

            assertTrue(namesList.contains("Pharmacy_2"));

            assertTrue(namesList.contains("Pharmacy_3"));
        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findAllPharmacies_invalidType_notReadablePropertyException() throws Exception {
        try {

            refreshDatabase();

            List<MedicineDto> allPharmacies = subject.findAllPharmacies(MedicineDto.class);

            assertThrows(NotReadablePropertyException.class, () -> {
                for (MedicineDto pharmacy : allPharmacies) {
                    String title = pharmacy.getTitle();
                    assertFalse(title.isBlank());
                }
            });
        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findAllPharmacies_validVariableTypeAndNullReturnType_ok() throws Exception {
        try {

            refreshDatabase();

            List<Pharmacy> allPharmacies = subject.findAllPharmacies(null);

            for (Pharmacy pharmacy : allPharmacies) {
                long l = pharmacy.getId();
            }

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findAllPharmacies_invalidVariableTypeAndNullReturnType_ClassCastException() throws Exception {
        try {

            refreshDatabase();

            assertThrows(ClassCastException.class, () -> {
                List<PriceDto> allPharmacies = subject.findAllPharmacies(null);

                for (PriceDto priceDto : allPharmacies) {
                    long priceValue = priceDto.getPrice().longValue();

                    assertTrue(priceValue > 0);
                }
            });

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findByName_existingName_optionalIsPresent() throws Exception {

        try {

            refreshDatabase();

            Optional<Pharmacy> pharmacyOptional = subject.findByName("Pharmacy_1");

            assertTrue(pharmacyOptional.isPresent());

            final var expectedName = "Pharmacy_1";

            long expectedId = 2021102501;

            Pharmacy pharmacy = pharmacyOptional.get();

            System.out.println(pharmacy);

            assertEquals(expectedName, pharmacy.getName());

            assertEquals(expectedId, pharmacy.getId());

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findByName_nonExistingName_optionalIsEmpty() throws Exception {
        try {

            refreshDatabase();

            Optional<Pharmacy> pharmacyOptional = subject.findByName("Pharmacy_1000");

            assertTrue(pharmacyOptional.isEmpty());

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findByName_null_optionalIsEmpty() throws Exception {
        try {

            refreshDatabase();

            Optional<Pharmacy> pharmacyOptional = subject.findByName(null);

            assertTrue(pharmacyOptional.isEmpty());

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findById_existingId_ok() throws Exception {

        try {
            final long expectedId = 2021102501;
            final String expectedName = "Pharmacy_1";
            refreshDatabase();

            Optional<Pharmacy> pharmacyOptional = subject.findById(expectedId);

            assertTrue(pharmacyOptional.isPresent());

            Pharmacy pharmacy = pharmacyOptional.get();

            assertEquals(expectedId, pharmacy.getId());

            assertEquals(expectedName, pharmacy.getName());
        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findById_nonExistingId_emptyOptional() throws Exception {

        try {
            refreshDatabase();

            Optional<Pharmacy> pharmacyOptional = subject.findById(Long.MAX_VALUE);

            assertTrue(pharmacyOptional.isEmpty());
        } finally {
            dataSourceConnection.close();
        }
    }


    private void refreshDatabase() throws DatabaseUnitException, SQLException, IOException {
        DatabaseOperation.REFRESH.execute(dataSourceConnection, readDataset("datasets/Pharmacy_dataset.xml"));
    }
}