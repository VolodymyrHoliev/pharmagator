package com.eleks.academy.pharmagator.dataproviders.registry;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
class ResultTableParser {

    public Stream<MedicineRegistryRecordDto> parse(Elements tableRowElements) {
        return tableRowElements.stream().map(this::getResultFromSingleRow);
    }

    private MedicineRegistryRecordDto getResultFromSingleRow(Element tableRowElement) {
        String title = extractTitle(tableRowElement);

        String manufacturer = extractManufacturer(tableRowElement);

        String dosageForm = extractDosageForm(tableRowElement);

        String registryId = extractRegistryId(tableRowElement);

        return MedicineRegistryRecordDto.builder()
                .registrationId(registryId)
                .dosageForm(dosageForm)
                .manufacturer(manufacturer)
                .title(title)
                .build();
    }

    private String extractRegistryId(Element tableRowElement) {
        Element registryIdTableData = tableRowElement.child(0);

        return registryIdTableData.select("a").get(0).ownText();
    }

    private String extractTitle(Element tableRowElement) {
        Element titleTableData = tableRowElement.child(2);

        return titleTableData.ownText().replace("®", "");
    }

    private String extractDosageForm(Element tableRowElement) {
        Element titleTableData = tableRowElement.child(2);

        return titleTableData.select("span").get(0).ownText();
    }

    private String extractManufacturer(Element tableRowElement) {
        Element manufacturerTableData = tableRowElement.child(4);

        return manufacturerTableData.ownText();
    }

}
