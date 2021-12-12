package com.eleks.academy.pharmagator.dataproviders.registry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicineRegistryProvider implements IMedicineRegistryProvider {

    private final ResultTableParser tableParser;

    @Value("${pharmagator.drlz-registry.medicine-fetch-url}")
    private String baseUrl;

    @Override
    public Stream<MedicineRegistryRecordDto> fetchRecordsByRegistrationId(String registrationId) {
        String url = baseUrl + "&rs=" + registrationId;

        try {
            Document document = Jsoup.connect(url).get();

            Elements tableRows = getResultTableRows(document);

            return tableParser.parse(tableRows);
        } catch (IOException e) {
            log.error(e.getMessage(), e.getCause());

            return Stream.empty();
        }
    }

    private Elements getResultTableRows(Document document) {
        return document.select(new Evaluator.AttributeWithValue("bordercolor", "#DDDDDD"))
                .get(0)
                .children()
                .select("tr[valign=top]");
    }

}
