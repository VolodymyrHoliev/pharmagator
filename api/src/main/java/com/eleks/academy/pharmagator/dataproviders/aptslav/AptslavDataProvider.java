package com.eleks.academy.pharmagator.dataproviders.aptslav;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.AptslavResponseBody;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.AptslavMedicineDto;
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

    private final WebClient aptslavWebClient;
    @Value("${pharmagator.data-providers.aptslav.categories-url}")
    private String categoriesFetchUrl;
    @Value("${pharmagator.data-providers.aptslav.medicines-uri}")
    private String medicinesFetchUri;
    private final ApiDtoConverter<AptslavMedicineDto> apiDtoConverter;

    @Override
    public Stream<MedicineDto> loadData() {
        return fetchMedicines();
    }

    /**
     * @param step - how many objects we can retrieve,represents API`s 'take' parameter,
     *             according to API max value is 100,default value is 5
     * @param skip - how many objects we already have,represents API`s 'skip' parameter
     * @return ApiResponseBody
     * @see AptslavResponseBody
     */
    private AptslavResponseBody<AptslavMedicineDto> sendGetMedicinesRequest(int step, int skip) {
        return aptslavWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(medicinesFetchUri)
                        .queryParam("fields", "id,externalId,name,created")
                        .queryParam("take", step)
                        .queryParam("skip", skip)
                        .queryParam("inStock", true)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AptslavResponseBody<AptslavMedicineDto>>() {
                })
                .block();
    }

    /**
     * Here in this method we`re sending GET requests to the API until we`ve read all available data
     * While API`s 'take' parameter can`t be more than 100,we can`t fetch all available data in one
     * request
     * For now we`re taking only those products,which are in medicines category
     *
     * @return Stream<MedicineDto>
     */
    private Stream<MedicineDto> fetchMedicines() {
        Stream<AptslavMedicineDto> resultStream = Stream.empty();
        int step = 100;
        int fetchedObjectsCounter = 0;
        AptslavResponseBody<AptslavMedicineDto> initialResponse = sendGetMedicinesRequest(step, 0);
        long dataSetCount = initialResponse.getCount();
        List<AptslavMedicineDto> initialResponseData = initialResponse.getData();
        resultStream = Stream.concat(resultStream, initialResponseData.stream());
        while (fetchedObjectsCounter <= dataSetCount) {
            fetchedObjectsCounter += step;
            List<AptslavMedicineDto> nextResponseData = sendGetMedicinesRequest(step, fetchedObjectsCounter).getData();
            if (nextResponseData.isEmpty()) {
                break;
            } else {
                resultStream = Stream.concat(resultStream, nextResponseData.stream());
            }
        }
        return resultStream
                .map(apiDtoConverter::toMedicineDto);
    }
}
