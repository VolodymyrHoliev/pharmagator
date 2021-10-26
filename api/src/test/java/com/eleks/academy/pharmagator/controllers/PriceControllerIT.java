package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.AbstractDataIT;
import com.eleks.academy.pharmagator.JsonWriter;
import com.eleks.academy.pharmagator.controllers.requests.PriceRequest;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceControllerIT extends AbstractDataIT {

    private final String DATASET_FILE = "datasets/Price_dataset.xml";
    private MockMvc mockMvc;
    private DatabaseDataSourceConnection connection;

    @Autowired
    public void setComponents(final MockMvc mockMvc,
                              final DataSource dataSource) throws SQLException {
        this.mockMvc = mockMvc;
        this.connection = new DatabaseDataSourceConnection(dataSource);
    }

    @Test
    public void findAllPrices_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders.get("/prices"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].price",
                            hasItems(25.00, 50.00, 47.50)));
        } finally {
            this.connection.close();
        }
    }

    @Test
    public void findById_ok() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";
        final long pharmacyId = 2021102601;
        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders.get(url, pharmacyId, medicineId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.price", is(50.0)));
        } finally {
            this.connection.close();
        }
    }

    @Test
    public void findById_nonExistingEntity_ResponseStatusException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";
        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get(url,
                                    Long.MAX_VALUE, medicineId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode", is(404)))
                    .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ResponseStatusException));

        } finally {
            connection.close();
        }
    }

    @Test
    public void findById_invalidIdValue_MethodArgumentNotValidException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";
        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get(url, -1L, medicineId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ConstraintViolationException));

        } finally {
            connection.close();
        }
    }

    @Test
    public void create_validRequest_ok() throws Exception{
        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        final long medicineId = 2021102603;

        try {

            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            PriceRequest request = new PriceRequest(BigDecimal.valueOf(10.0), "extId");

            String requestBodyJson = JsonWriter.write(request);

            System.out.println(requestBodyJson);

            mockMvc.perform(MockMvcRequestBuilders.post(url, pharmacyId, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(requestBodyJson))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.price", is(10.0)));
        } finally {
            connection.close();
        }
    }

    @Test
    public void create_entityAlreadyExists_ConstraintViolationException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        final long medicineId = 2021102602;

        try {

            DatabaseOperation.REFRESH.execute(connection, readDataset(DATASET_FILE));

            PriceRequest request = new PriceRequest(BigDecimal.TEN, "extId");

            String requestBodyJson = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.post(url, pharmacyId, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(requestBodyJson))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result -> {
                        assertTrue(result.getResolvedException() instanceof IllegalArgumentException);
                    });
        } finally {
            connection.close();
        }
    }

    @Test
    public void create_negativePriceValue_MethodArgumentNotValidException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        final long medicineId = 2021102602;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(-100.00), "extId");
        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.post(url, pharmacyId, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        } finally {
            connection.close();
        }
    }

    @Test
    public void create_emptyExternalId_MethodArgumentNotValidException() throws Exception {
        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        final long medicineId = 2021102602;

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            PriceRequest request = new PriceRequest(BigDecimal.valueOf(100.00), "  ");

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.post(url, pharmacyId, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        } finally {
            connection.close();
        }
    }

    @Test
    public void create_negativePharmacyIdValue_MethodArgumentNotValidException() throws Exception {
        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long medicineId = 2021102602;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(100.00), "extId");

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.post(url, -2, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
        } finally {
            connection.close();
        }
    }

    @Test
    public void create_negativeMedicineIdValue_IllegalArgumentException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(100.00), "extId");

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.post(url, pharmacyId, 0)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
        } finally {
            connection.close();
        }
    }

    @Test
    public void update_validRequestBody_ok() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        final long medicineId = 2021102602;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(15.0), "extId");

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String requestBodyJson = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.put(url, pharmacyId, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(requestBodyJson))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.price", is(15.0)));
        } finally {
            connection.close();
        }
    }

    @Test
    public void update_negativePriceValue_MethodArgumentNotValidException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        final long medicineId = 2021102602;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(-100.00), "extId");

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.put(url, pharmacyId, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        } finally {
            connection.close();
        }
    }

    @Test
    public void update_emptyExternalId_MethodArgumentNotValidException() throws Exception {
        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        final long medicineId = 2021102602;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(100.00), "  ");

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.put(url, pharmacyId, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
        } finally {
            connection.close();
        }
    }

    @Test
    public void update_negativePharmacyIdValue_MethodArgumentNotValidException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long medicineId = 2021102602;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(100.00), "extId");

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.put(url, -2, medicineId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
        } finally {
            connection.close();
        }
    }

    @Test
    public void update_negativeMedicineIdValue_MethodArgumentNotValidException() throws Exception {

        final String url = "/prices/pharmacies/{pharmacyId}/medicines/{medicineId}";

        final long pharmacyId = 2021102601;

        PriceRequest request = new PriceRequest(BigDecimal.valueOf(100.00), "extId");

        try {
            DatabaseOperation.REFRESH.execute(this.connection, readDataset(DATASET_FILE));

            String json = JsonWriter.write(request);

            mockMvc.perform(MockMvcRequestBuilders.put(url, pharmacyId, 0)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(json))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
        } finally {
            connection.close();
        }
    }


}