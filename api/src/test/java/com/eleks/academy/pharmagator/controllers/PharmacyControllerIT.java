package com.eleks.academy.pharmagator.controllers;

import com.eleks.academy.pharmagator.JsonWriter;
import com.eleks.academy.pharmagator.controllers.requests.PharmacyRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
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
import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PharmacyControllerIT {

    private MockMvc mockMvc;
    private DatabaseDataSourceConnection dataSourceConnection;

    @Autowired
    public void setComponents(final MockMvc mockMvc,
                              final DataSource dataSource) throws SQLException {
        this.mockMvc = mockMvc;
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource);
    }

    @Test
    public void findAllPharmacies_findIds_ok() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders.get("/pharmacies"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$[*].name",
                            Matchers.hasItems("PharmacyControllerIT_name1", "PharmacyControllerIT_name2")));
        } finally {
            this.dataSourceConnection.close();
        }
    }

    @Test
    public void findById_existingId_ok() throws Exception {
        final long id = 2021102101;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/pharmacies/{pharmacyId}", id))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name",
                            is("PharmacyControllerIT_name1")));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void findById_nonExistingId_ResponseStatusException() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .get("/pharmacies/{pharmacyId}", Long.MAX_VALUE))
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
    public void create_validPharmacyRequest_ok() throws Exception {

        PharmacyRequest request = new PharmacyRequest("New pharmacy", "link_template");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/pharmacies/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.name", is("New pharmacy")))
                .andExpect(jsonPath("$.medicineLinkTemplate", is("link_template")));
    }

    @Test
    public void create_invalidPharmacyRequest_MethodArgumentNotValidException() throws Exception {
        PharmacyRequest request = new PharmacyRequest(null, "");

        String json = JsonWriter.write(request);
        mockMvc.perform(MockMvcRequestBuilders.post("/pharmacies/")
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
        mockMvc.perform(MockMvcRequestBuilders.post("/pharmacies/")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException));
    }

    @Test
    public void deleteById_ok() throws Exception {
        final long id = 2021102102;
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .delete("/pharmacies/{pharmacyId}", id))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void deleteById_nonExistingId_exc() throws Exception {
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .delete("/pharmacies/{pharmacyId}", Long.MAX_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof EmptyResultDataAccessException));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void update_validRequest_ok() throws Exception {
        final long id = 2021102102;
        PharmacyRequest request = new PharmacyRequest("Updated", "link");
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/pharmacies/{pharmacyId}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(JsonWriter.write(request)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.name", is("Updated")))
                    .andExpect(jsonPath("$.medicineLinkTemplate", is("link")));

        } finally {
            dataSourceConnection.close();
        }
    }

    @Test
    public void update_nonExistingId_ResponseStatusException() throws Exception {
        PharmacyRequest request = new PharmacyRequest("Updated", "link");
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/pharmacies/{pharmacyId}", Long.MAX_VALUE)
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
        PharmacyRequest request = new PharmacyRequest("", null);
        try {
            DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());

            this.mockMvc.perform(MockMvcRequestBuilders
                            .put("/pharmacies/{pharmacyId}", id)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(JsonWriter.write(request)))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(result ->
                            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

        } finally {
            dataSourceConnection.close();
        }
    }


    private IDataSet readDataset() throws DataSetException, IOException {
        try (var resource = getClass()
                .getResourceAsStream("PharmacyControllerIT_dataset.xml")) {
            return new FlatXmlDataSetBuilder()
                    .build(resource);
        }
    }

}
