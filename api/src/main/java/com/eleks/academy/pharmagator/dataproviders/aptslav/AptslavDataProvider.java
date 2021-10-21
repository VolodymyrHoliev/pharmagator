package com.eleks.academy.pharmagator.dataproviders.aptslav;

import com.eleks.academy.pharmagator.dataproviders.DataProvider;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.AptslavMedicineDto;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.AptslavResponseBody;
import com.eleks.academy.pharmagator.dataproviders.aptslav.dto.converters.ApiDtoConverter;
import com.eleks.academy.pharmagator.dataproviders.dto.MedicineDto;
import com.eleks.academy.pharmagator.repositories.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Qualifier("aptslavDataProvider")
public class AptslavDataProvider implements DataProvider {
    @Value("${pharmagator.data-providers.aptslav.categories-url}")
    private String categoriesFetchUrl;
    @Value("${pharmagator.data-providers.aptslav.medicines-uri}")
    private String medicinesFetchUri;
    @Value("${pharmagator.data-providers.aptslav.medicineLinkTemplate}")
    private String medicineLinkTemplate;
    @Value("${pharmagator.data-providers.aptslav.title}")
    private String pharmacyTitle;

    private final WebClient aptslavWebClient;
    private final ApiDtoConverter<AptslavMedicineDto> apiDtoConverter;
    private final PharmacyRepository pharmacyRepository;

    @Override
    public Stream<MedicineDto> loadData() {
        return fetchMedicines();
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

        int step = 100;

        AptslavResponseBody<AptslavMedicineDto> initialResponse = sendGetMedicinesRequest(step, 0);

        long dataSetCount = initialResponse.getCount();

        long steps = dataSetCount / step;

        Stream<AptslavMedicineDto> restOfData = LongStream.rangeClosed(1, steps)
                .boxed()
                .map(s -> sendGetMedicinesRequest(step, (int) (s * step)))
                .map(AptslavResponseBody::getData)
                .flatMap(Collection::stream);

        return Stream.concat(initialResponse.getData().stream(), restOfData)
                .map(apiDtoConverter::toMedicineDto);
    }

    /**
     * @param step - how many objects we can retrieve,represents API`s 'take' parameter.
     *             According to API, max value is 100,default value is 5
     * @param skip - how many objects we already have,represents API`s 'skip' parameter
     * @return AptslavResponseBody<AptslavMedicineDto>
     * @see AptslavResponseBody
     */
    private AptslavResponseBody<AptslavMedicineDto> sendGetMedicinesRequest(int step, int skip) {

        return aptslavWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(medicinesFetchUri)
                        .queryParam("fields", "id,externalId,name,created,manufacturer")
                        .queryParam("take", step)
                        .queryParam("skip", skip)
                        .queryParam("inStock", true)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<AptslavResponseBody<AptslavMedicineDto>>() {
                })
                .block();
    }
}
