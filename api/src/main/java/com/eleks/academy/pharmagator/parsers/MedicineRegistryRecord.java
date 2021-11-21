package com.eleks.academy.pharmagator.parsers;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This DTO represents a record from csv/drlz_registry_utf-8.csv
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRegistryRecord {

    @Parsed(field = "Торгівельне найменування")
    private String title;

    @Parsed(field = "Номер Реєстраційного посвідчення")
    private String registrationId;

    @Parsed(field = "Дата початку дії")
    private String licensedAt;

    @Parsed(field = "Заявник: назва українською")
    private String applicantName;

    @Parsed(field = "Заявник: країна")
    private String applicantCountry;

    @Parsed(field = "Кількість виробників")
    private String numberOfManufacturers;

    @Parsed(field = "Форма випуску")
    private String dosageForm;

    @Parsed(field = "Склад (діючі)")
    private String compositionOfActiveSubstances;

    @ParsedCollection(fields = {"Виробник 1: назва українською",
            "Виробник 2: назва українською", "Виробник 3: назва українською",
            "Виробник 4: назва українською",
            "Виробник 5: назва українською"})
    private List<String> manufacturers;

    @ParsedCollection(fields = {"Виробник 1: країна",
            "Виробник 2: країна", "Виробник 3: країна", "Виробник 4: країна",
            "Виробник 5: країна"})
    private List<String> manufacturersCountries;

}
