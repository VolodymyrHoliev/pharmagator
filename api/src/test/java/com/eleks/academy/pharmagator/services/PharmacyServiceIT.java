package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.AbstractDataIT;
import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.eleks.academy.pharmagator.converters.request.PharmacyRequestMapper;
import com.eleks.academy.pharmagator.entities.Pharmacy;
import com.eleks.academy.pharmagator.projections.PharmacyDto;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PharmacyServiceIT extends AbstractDataIT {
    private final String DATASET_FILE = "datasets/Pharmacy_dataset.xml";

    @SpyBean
    private PharmacyRepository pharmacyRepository;
    @SpyBean
    private PharmacyRequestMapper requestMapper;
    @Spy
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
    private DatabaseDataSourceConnection connection;

    private PharmacyService subject;

    @Autowired
    void setComponents(PharmacyRepository pharmacyRepository,
                       PharmacyRequestMapper pharmacyRequestMapper, DataSource dataSource) throws SQLException {
        this.pharmacyRepository = pharmacyRepository;
        this.requestMapper = pharmacyRequestMapper;
        this.connection = new DatabaseDataSourceConnection(dataSource);
        subject = new PharmacyService(pharmacyRepository, requestMapper, projectionFactory);

    }

    @Test
    void save_validRequest_ok() {
        final String expectedTitle = "Pharmacy";
        final String expectedLink = "link";

        PharmacyRequest pharmacyRequest = new PharmacyRequest(expectedTitle, expectedLink);

        PharmacyDto savedObjectDto = subject.save(pharmacyRequest);

        InOrder order = inOrder(requestMapper, pharmacyRepository, projectionFactory);

        order.verify(requestMapper).toEntity(pharmacyRequest);

        order.verify(pharmacyRepository).save(any(Pharmacy.class));

        order.verify(projectionFactory).createProjection(any(), any(Pharmacy.class));

        assertEquals(expectedTitle, savedObjectDto.getName());

        assertEquals(expectedLink, savedObjectDto.getMedicineLinkTemplate());

    }

    @Test
    void save_nullRequest_IllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> subject.save(null));

        verify(requestMapper, times(1)).toEntity(null);
    }

    @Test
    void update_validRequest_ok() throws Exception {
        final long expectedId = 2021102503L;
        final String expectedTitle = "Updated name";
        final String expectedLink = "updated_link";

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            subject.update(expectedId, new PharmacyRequest(expectedTitle, expectedLink));

            InOrder order = inOrder(pharmacyRepository, projectionFactory);

            order.verify(pharmacyRepository).findById(expectedId, Pharmacy.class);

            Pharmacy updatedPharmacy = new Pharmacy(expectedId, expectedTitle, expectedLink);

            order.verify(pharmacyRepository).save(updatedPharmacy);

            order.verify(projectionFactory).createProjection(PharmacyDto.class, updatedPharmacy);
        } finally {
            connection.close();
        }

    }

    @Test
    void update_nonExistingId_ResponseStatusException() {
        assertThrows(ResponseStatusException.class, () ->
                subject
                        .update(Long.MAX_VALUE, new PharmacyRequest("", "")));
    }

    @Test
    void update_null_IllegalArgumentException() {
        final long id = 2021102501;
        assertThrows(IllegalArgumentException.class, () ->
                subject
                        .update(id, null));
    }

    @Test
    void update_requestWithNullProperty_IllegalArgumentException() {
        final long id = 2021102501;
        assertThrows(IllegalArgumentException.class, () ->
                subject
                        .update(id, new PharmacyRequest(null, "link")));
    }

    @Test
    void findAll_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            List<PharmacyDto> all = subject.findAll();

            List<String> namesList = all.stream()
                    .map(PharmacyDto::getName)
                    .collect(Collectors.toList());

            assertTrue(namesList.contains("Pharmacy_1"));
            assertTrue(namesList.contains("Pharmacy_2"));
            assertTrue(namesList.contains("Pharmacy_3"));
        } finally {
            connection.close();
        }
    }

    @Test
    void findById_existingId_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));
            final long id = 2021102501;
            final String expectedName = "Pharmacy_1";

            PharmacyDto dto = subject.findById(id);

            assertEquals(expectedName, dto.getName());
        } finally {
            connection.close();
        }

    }

    @Test
    void findById_nonExistingId_ResponseStatusException() {

        assertThrows(ResponseStatusException.class, () -> subject.findById(Long.MAX_VALUE));
    }

    @Test
    void deleteById_existingId_ok() throws Exception {
        final long id = 2021102502;
        try {

            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            subject.delete(id);

            verify(pharmacyRepository).deleteById(id);

            List<Long> idList = pharmacyRepository.findAll()
                    .stream()
                    .map(Pharmacy::getId)
                    .collect(Collectors.toList());

            assertFalse(idList.contains(id));
        } finally {
            connection.close();
        }
    }
}