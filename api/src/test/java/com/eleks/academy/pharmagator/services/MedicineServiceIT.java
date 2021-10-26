package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.AbstractDataIT;
import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.eleks.academy.pharmagator.converters.request.MedicineRequestMapper;
import com.eleks.academy.pharmagator.entities.Medicine;
import com.eleks.academy.pharmagator.projections.MedicineDto;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
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
class MedicineServiceIT extends AbstractDataIT {

    private final String DATASET_FILE = "datasets/Medicine_dataset.xml";
    private DatabaseDataSourceConnection connection;

    @SpyBean
    private MedicineRepository medicineRepository;

    @SpyBean
    private MedicineRequestMapper mapper;

    @Spy
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    private MedicineService subject;

    @Autowired
    public void setComponents(MedicineRepository repository, MedicineRequestMapper mapper,
                              DataSource dataSource) throws SQLException {
        this.medicineRepository = repository;

        this.mapper = mapper;

        this.subject = new MedicineService(medicineRepository, mapper, projectionFactory);

        this.connection = new DatabaseDataSourceConnection(dataSource);
    }

    @Test
    public void findAll_ok() throws Exception {

        DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

        List<MedicineDto> all = subject.findAll();

        List<String> titles = all.stream()
                .map(MedicineDto::getTitle)
                .collect(Collectors.toList());

        assertTrue(titles.contains("Medicine_1"));
        assertTrue(titles.contains("Medicine_2"));

        verify(medicineRepository).findAll(MedicineDto.class);
    }

    @Test
    public void save_validRequest_ok() {
        final String expectedTitle = "Medicine_3";

        MedicineRequest request = new MedicineRequest(expectedTitle);

        MedicineDto savedEntity = subject.save(request);

        InOrder order = inOrder(mapper, medicineRepository, projectionFactory);

        order.verify(mapper).toEntity(request);

        order.verify(medicineRepository).save(any(Medicine.class));

        order.verify(projectionFactory).createProjection(any(), any(Medicine.class));

        assertEquals(request.getTitle(), savedEntity.getTitle());
    }

    @Test
    void save_nullRequest_IllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () -> subject.save(null));

        verify(mapper, times(1)).toEntity(null);
    }

    @Test
    void save_requestWithNullProperty_IllegalArgumentException() {

        MedicineRequest medicineRequest = new MedicineRequest(null);
        assertThrows(IllegalArgumentException.class, () -> subject.save(medicineRequest));

        verify(mapper, times(1)).toEntity(medicineRequest);
    }

    @Test
    void update_validRequest_ok() throws Exception {
        final long expectedId = 2021102501;
        final String expectedTitle = "Updated title";

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            MedicineRequest medicineRequest = new MedicineRequest(expectedTitle);

            subject.update(expectedId, medicineRequest);

            InOrder order = inOrder(mapper, medicineRepository, projectionFactory);

            order.verify(mapper).toEntity(medicineRequest);

            Medicine updatedMedicine = new Medicine(expectedId, expectedTitle);

            order.verify(medicineRepository).save(updatedMedicine);

            order.verify(projectionFactory).createProjection(MedicineDto.class, updatedMedicine);

        } finally {

            connection.close();
        }

    }

    @Test
    void update_nonExistingId_ResponseStatusException() {
        assertThrows(ResponseStatusException.class, () ->
                subject
                        .update(Long.MAX_VALUE, new MedicineRequest("  ")));
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
                        .update(id, new MedicineRequest(null)));
    }

    @Test
    void findById_existingId_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));
            final long id = 2021102501;
            final String expectedTitle = "Medicine_1";

            MedicineDto dto = subject.findById(id);

            assertEquals(expectedTitle, dto.getTitle());
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
        final long id = 2021102501;
        try {

            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            subject.delete(id);

            verify(medicineRepository).deleteById(id);

            List<Long> idList = medicineRepository.findAll()
                    .stream()
                    .map(Medicine::getId)
                    .collect(Collectors.toList());

            assertFalse(idList.contains(id));
        } finally {
            connection.close();
        }
    }
}