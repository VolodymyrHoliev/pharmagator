package com.eleks.academy.pharmagator.dataproviders;

import org.springframework.beans.factory.annotation.Value;

public class PharmacySlavDataProvider {
    @Value("${pharmagator.data-providers.apteka-ds.url}")
    private String baseUrl;

    @Value("${pharmagator.data-providers.apteka-ds.url}"
            + "${pharmagator.data-providers.apteka-ds.category-fetch-url}")
    private String categoriesFetchUrl;

    @Value("${pharmagator.data-providers.apteka-ds.category-path}")
    private String categoryPathEnding;

    /*
      в loadData() вместо url(slug) нужно передать id -
      и тогда в fetchMedicinesByCategory(int id)
      uri будет что-то типа этого:
      .uri(baseUrl + "/" + id + categoryPathEnding)
     */

}
