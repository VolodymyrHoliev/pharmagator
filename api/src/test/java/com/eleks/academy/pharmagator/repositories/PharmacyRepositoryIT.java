package com.eleks.academy.pharmagator.repositories;

import com.eleks.academy.pharmagator.projections.PharmacyDto;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class PharmacyRepositoryIT {

    private PharmacyRepository subject;
    private DatabaseDataSourceConnection dataSourceConnection;


    @Autowired
    public void setSubject(final PharmacyRepository pharmacyRepository, DataSource dataSource) throws SQLException {
        this.subject = pharmacyRepository;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
    }
    @Test
    public void contextLoads() {
    }

    @Test
    public void findAllPharmacies_ok() throws Exception {
        try {

            DatabaseOperation.REFRESH.execute(dataSourceConnection, readDataset());

            List<PharmacyDto> allPharmacies = subject.findAllPharmacies(PharmacyDto.class);

            List<String> namesList = allPharmacies.stream()
                    .map(PharmacyDto::getName)
                    .collect(Collectors.toList());

            assertTrue(namesList.contains("Pharmacy_1"));
            assertTrue(namesList.contains("Pharmacy_2"));
            assertTrue(namesList.contains("Pharmacy_3"));

            System.out.println(allPharmacies);
        } finally {
            dataSourceConnection.close();
        }
    }

    private IDataSet readDataset() throws DataSetException, IOException {
        File file = ResourceUtils.getFile("src/test/resources/Pharmacy_dataset.xml");
        try (var resource = new FileInputStream(file)) {
            return new FlatXmlDataSetBuilder()
                    .build(resource);
        }
    }
}