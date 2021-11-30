package com.eleks.academy.pharmagator.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ViewControllerIT {

    private MockMvc mockMvc;

    private static final String URI = "/{page}";

    @Autowired
    void setComponents(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void homePage_ok() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void search_ok() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/1?searchQuery=&sortOrder=price:ASC"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void search_negativePagePathVarValue_IAE() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/-1?searchQuery=&sortOrder=price:ASC"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException));
    }

}