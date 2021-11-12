package com.eleks.academy.pharmagator.dataproviders;


import com.eleks.academy.pharmagator.dataproviders.dto.aptslav.converters.ApiMedicineDtoConverter;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AptslavDataProviderIT {

    private static MockWebServer mockWebServer;

    private AptslavDataProvider subject;

    private final String BASE_URL = "/aptslav.com.ua:3000/api/v1/categories/87/products";

    @BeforeAll
    static void beforeAll() throws IOException {

        mockWebServer = new MockWebServer();

        mockWebServer.start();

    }

    @AfterAll
    static void afterAll() throws IOException {

        mockWebServer.shutdown();

    }

    @Autowired
    public void setSubject(ApiMedicineDtoConverter apiMedicineDtoConverter) {

        HttpUrl url = mockWebServer.url(BASE_URL);

        WebClient webClient = WebClient.create(url.toString());

        subject = new AptslavDataProvider(webClient, apiMedicineDtoConverter);

        ReflectionTestUtils.setField(subject, "pageSize", 100);

    }

    @SuppressWarnings("ConstantConditions")
    private String getQueryParameter(RecordedRequest request, String paramName) {

        return request.getRequestUrl().queryParameter(paramName);

    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void setMedicineRequest_ok() throws InterruptedException {

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        );

        subject.sendGetMedicinesRequest(100, 10);

        RecordedRequest request = mockWebServer.takeRequest();

        String s = request.getBody().readUtf8();

        System.out.println(s);

        assertEquals("GET", request.getMethod());

        System.out.println(request.getPath());

        assertTrue(request.getPath().startsWith(BASE_URL));

        assertEquals("id,externalId,name,created,manufacturer",
                getQueryParameter(request, "fields"));

        assertEquals("100", getQueryParameter(request, "take"));

        assertEquals("10", getQueryParameter(request, "skip"));

        assertEquals("true", getQueryParameter(request, "inStock"));

    }


    @Test
    public void setMedicineRequest_invalidStepParam_IAE() {

        assertThrows(IllegalArgumentException.class,
                () -> subject.sendGetMedicinesRequest(1000, 10));

    }

    @Test
    public void calculateTotalPages_multipleOf100_ok() {

        long totalPages = subject.calculateTotalPages(1200);

        assertEquals(12, totalPages);

    }

    @Test
    public void calculateTotalPages_notMultipleOf100_ok() {

        long totalPages = subject.calculateTotalPages(1020);

        assertEquals(11, totalPages);

    }
}