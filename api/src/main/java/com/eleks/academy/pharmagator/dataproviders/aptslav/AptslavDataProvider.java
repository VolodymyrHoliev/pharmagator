package com.eleks.academy.pharmagator.dataproviders.aptslav;

import com.eleks.academy.pharmagator.converters.MedicineDtoConverter;
import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.ApiResponseBody;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.MedicineApiDto;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.converters.ApiDtoConverter;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Qualifier("aptslavDataProvider")
public class AptslavDataProvider implements DataProvider {
    private final WebClient webClient;
    @Value("${pharmagator.data-providers.aptslav.categories-url}")
    private String categoriesFetchUrl;
    @Value("${pharmagator.data-providers.aptslav.medicines-uri}")
    private String medicinesFetchUri;
    private MedicineDtoConverter<MedicineDto> medicineDtoConverter;
    private ApiDtoConverter<MedicineApiDto> apiDtoConverter;

    @Override
    public Stream<MedicineDto> loadData() {
       return fetchMedicines();
    }

    private ApiResponseBody<MedicineApiDto> sendGetMedicinesRequest(long step, long skip) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(medicinesFetchUri)
                        .queryParam("fields", "id,externalId,name,created")
                        .queryParam("take", step)
                        .queryParam("skip", skip)
                        .queryParam("inStock", true)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponseBody<MedicineApiDto>>() {
                })
                .block();
    }

    private Stream<MedicineDto> fetchMedicines() {
        Stream<MedicineApiDto> resultStream = Stream.empty();
        long step = 10;
        long stepCounter = 0;
        ApiResponseBody<MedicineApiDto> initialResponse = sendGetMedicinesRequest(step, 0);
        long dataSetCount = initialResponse.getCount();
        List<MedicineApiDto> initialResponseData = initialResponse.getData();
        resultStream = Stream.concat(resultStream, initialResponseData.stream());
        while (stepCounter <= dataSetCount) {
            stepCounter += step;
            List<MedicineApiDto> nextResponseData = sendGetMedicinesRequest(step, stepCounter).getData();
            if (nextResponseData.isEmpty()) {
                break;
            } else {
                resultStream = Stream.concat(resultStream, nextResponseData.stream());
            }
        }
        return resultStream
                .map(medicineApiDto -> apiDtoConverter.toMedicineDto(medicineApiDto));
    }

}
