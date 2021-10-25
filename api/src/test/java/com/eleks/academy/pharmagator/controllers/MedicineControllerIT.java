package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.AbstractDataIT;
import com.eleks.academy.pharmagator.JsonWriter;
import com.eleks.academy.pharmagator.controllers.requests.MedicineRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MedicineControllerIT extends AbstractDataIT {

    private final String DATASET_FILE = "Medicine_dataset.xml";
    private MockMvc mockMvc;
    private DatabaseDataSourceConnection dataSourceConnection;

    @Autowired
    public void setComponents(final MockMvc mockMvc,
                              final DataSource dataSource) throws SQLException {
        this.mockMvc = mockMvc;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
    }

    @Test
    public void findAllMedicines_findIds_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders.get("/medicines"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].title",
                            Matchers.hasItems("Medicine_1", "Medicine_2")));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findById_existingId_ok() throws Exception {
        final long id = 2021102501;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/medicines/{medicineId}", id))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.title",
                            is("Medicine_1")));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findById_nonExistingId_ResponseStatusException() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/medicines/{medicineId}", Long.MAX_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.statusCode", is(404)))
                    .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ResponseStatusException));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void create_validMedicineRequest_ok() throws Exception {

        MedicineRequest request = new MedicineRequest("New medicine");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/medicines/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", is("New medicine")));
    }

    @Test
    public void create_invalidMedicineRequest_MethodArgumentNotValidException() throws Exception {
        MedicineRequest request = new MedicineRequest("  ");

        String json = JsonWriter.write(request);
        mockMvc.perform(MockMvcRequestBuilders.post("/medicines/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    public void create_requestIsNull_HttpMessageNotReadableException() throws Exception {

        String json = JsonWriter.write(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/medicines/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException));
    }

    @Test
    public void deleteById_ok() throws Exception {
        final long id = 2021102502;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .delete("/medicines/{medicineId}", id))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void deleteById_nonExistingId_exc() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .delete("/medicines/{medicineId}", Long.MAX_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof EmptyResultDataAccessException));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void update_validRequest_ok() throws Exception {
        final long id = 2021102502;
        MedicineRequest request = new MedicineRequest("Updated");
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/medicines/{medicineId}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(JsonWriter.write(request)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.title", is("Updated")));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void update_nonExistingId_ResponseStatusException() throws Exception {
        MedicineRequest request = new MedicineRequest("Updated");
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/medicines/{medicineId}", Long.MAX_VALUE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(JsonWriter.write(request)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(jsonPath("$.status", is("NOT_FOUND")))
                    .andExpect(jsonPath("$.statusCode", is(404)))
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof ResponseStatusException));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void update_invalidRequestBodyProperties_MethodArgumentNotValidException() throws Exception {
        final long id = 2021102102;
        MedicineRequest request = new MedicineRequest("");
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset(DATASET_FILE));

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/medicines/{medicineId}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(JsonWriter.write(request)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        } finally {
            dataSourceConnection.close();
        }
    }


}