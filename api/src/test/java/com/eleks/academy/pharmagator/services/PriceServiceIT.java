package com.eleks.academy.pharmagator.services;

import com.eleks.academy.pharmagator.AbstractDataIT;
import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import com.eleks.academy.pharmagator.converters.request.PriceRequestMapper;
import com.eleks.academy.pharmagator.entities.Price;
import com.eleks.academy.pharmagator.projections.PriceDto;
import com.eleks.academy.pharmagator.repositories.MedicineRepository;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import com.eleks.academy.pharmagator.repositories.PriceRepository;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class PriceServiceIT extends AbstractDataIT {

    private final String DATASET_FILE = "datasets/Price_dataset.xml";

    @SpyBean
    private PriceRepository priceRepository;

    @SpyBean
    private PharmacyRepository pharmacyRepository;

    @SpyBean
    private MedicineRepository medicineRepository;

    @SpyBean
    private PriceRequestMapper requestMapper;

    @Spy
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    private DatabaseDataSourceConnection connection;

    private PriceService subject;

    @Autowired
    public void setComponents(PriceRepository priceRepository, PriceRequestMapper priceRequestMapper,
                              PharmacyRepository pharmacyRepository, MedicineRepository medicineRepository,
                              DataSource dataSource) throws SQLException {

        connection = new DatabaseDataSourceConnection(dataSource);

        this.priceRepository = priceRepository;

        this.requestMapper = priceRequestMapper;

        this.pharmacyRepository = pharmacyRepository;

        this.medicineRepository = medicineRepository;

        subject = new PriceService(this.priceRepository,
                this.pharmacyRepository, this.medicineRepository,
                requestMapper, projectionFactory);
    }

    @Test
    public void findAll_ok() throws Exception {

        final double firstPriceExpectedValue = 25.0;
        final double secondPriceExpectedValue = 50.0;
        final double thirdPriceExpectedValue = 47.50;
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            List<PriceDto> all = subject.findAll();

            verify(priceRepository).findAll(PriceDto.class);

            List<Double> prices = all
                    .stream()
                    .map(PriceDto::getPrice)
                    .map(BigDecimal::doubleValue)
                    .collect(Collectors.toList());

            assertTrue(prices.contains(firstPriceExpectedValue));

            assertTrue(prices.contains(secondPriceExpectedValue));

            assertTrue(prices.contains(thirdPriceExpectedValue));

        } finally {
            connection.close();
        }
    }

    @Test
    public void findById_validPharmacyAndMedicineId_ok() throws Exception {

        final long pharmacyId = 2021102601;
        final long medicineId = 2021102602;
        final double expectedPriceValue = 50.0;
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            PriceDto priceDto = subject.findById(medicineId, pharmacyId);

            verify(priceRepository).findByMedicineIdAndPharmacyId(medicineId, pharmacyId, PriceDto.class);

            assertEquals(expectedPriceValue, priceDto.getPrice().doubleValue());

        } finally {

            connection.close();
        }

    }

    @Test
    public void findById_nonExistingPharmacyId_ResponseStatusException() throws Exception {

        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            assertThrows(ResponseStatusException.class, () -> subject.findById(medicineId, Long.MAX_VALUE));

        } finally {
            connection.close();
        }
    }

    @Test
    public void findById_nonExistingMedicineId_ResponseStatusException() throws Exception {

        final long pharmacyId = 2021102601;

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            assertThrows(ResponseStatusException.class, () -> subject.findById(Long.MAX_VALUE, pharmacyId));

        } finally {
            connection.close();
        }
    }

    @Test
    public void save_validRequest_ok() throws Exception {

        final BigDecimal expectedPriceValue = BigDecimal.valueOf(10.25);
        final String expectedExternalId = "extId";
        final long medicineId = 2021102603;
        final long pharmacyId = 2021102602;
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            PriceRequest request = new PriceRequest(expectedPriceValue, expectedExternalId);

            PriceDto priceDto = subject.save(request, medicineId, pharmacyId);

            InOrder order = inOrder(requestMapper, medicineRepository, pharmacyRepository,
                    priceRepository, projectionFactory);

            order.verify(requestMapper).toEntity(request);

            order.verify(medicineRepository).findById(medicineId);

            order.verify(pharmacyRepository).findById(pharmacyId);

            order.verify(priceRepository).save(any(Price.class));

            order.verify(projectionFactory).createProjection(any(), any(Price.class));

            assertEquals(expectedPriceValue, priceDto.getPrice());
        } finally {
            connection.close();
        }
    }

    @Test
    public void save_nullRequest_IllegalArgumentException() {

        assertThrows(IllegalArgumentException.class,
                () -> subject.save(null, 2021102601L, 2021102601L));
    }

    @Test
    public void save_validRequestAndNonExistingMedicineId_ResponseStatusException() throws Exception {

        final long pharmacyId = 2021102601;
        PriceRequest request = new PriceRequest(BigDecimal.valueOf(10.5), "extId");
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            assertThrows(IllegalArgumentException.class,
                    () -> subject.save(request, Long.MAX_VALUE, pharmacyId));

        } finally {
            connection.close();
        }
    }

    @Test
    public void save_validRequestAndNonExistingPharmacyId_ResponseStatusException() throws Exception {

        final long medicineId = 2021102601;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(10.5), "extId");
        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            assertThrows(IllegalArgumentException.class,
                    () -> subject.save(request, medicineId, Long.MAX_VALUE));

        } finally {
            connection.close();
        }
    }

    @Test
    public void update_validRequest_ok() throws Exception {
        PriceRequest priceRequest = new PriceRequest(BigDecimal.TEN, "extId");
        final long pharmacyId = 2021102601;
        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            subject.update(medicineId, pharmacyId, priceRequest);

            InOrder order = inOrder(priceRepository, projectionFactory);

            order.verify(priceRepository).findByMedicineIdAndPharmacyId(medicineId, pharmacyId, Price.class);

            Price price = requestMapper.toEntity(priceRequest);

            price.setPharmacyId(pharmacyId);

            price.setMedicineId(medicineId);

            price.setExternalId("extId");

            order.verify(priceRepository).save(any(Price.class));

            order.verify(projectionFactory).createProjection(any(), any(Price.class));

        } finally {
            connection.close();
        }
    }

    @Test
    public void update_nullRequest_IllegalArgumentException() throws Exception {
        final long pharmacyId = 2021102601;
        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            assertThrows(IllegalArgumentException.class,
                    () -> subject.update(medicineId, pharmacyId, null));
        } finally {
            connection.close();
        }
    }

    @Test
    public void update_nonExistingEntity_ResponseStatusException() throws Exception {
        PriceRequest priceRequest = new PriceRequest(BigDecimal.TEN, "extId");
        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            assertThrows(ResponseStatusException.class,
                    () -> subject.update(medicineId, Long.MAX_VALUE, priceRequest));
        } finally {
            connection.close();
        }
    }

    @Test
    public void deleteById_validRequest_ok() throws Exception {
        final long pharmacyId = 2021102601;
        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            subject.delete(medicineId, pharmacyId);

            verify(priceRepository).deleteByMedicineIdAndPharmacyId(medicineId, pharmacyId);

        } finally {

            connection.close();
        }

    }

    @Test
    public void deleteById_nonExistingEntity_ok() throws Exception {
        final long pharmacyId = 2021102601;

        try {
            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            assertThrows(IllegalArgumentException.class,
                    () -> subject.delete(Long.MAX_VALUE, pharmacyId));

            verify(priceRepository, never()).deleteByMedicineIdAndPharmacyId(Long.MAX_VALUE, pharmacyId);

        } finally {

            connection.close();
        }

    }
}