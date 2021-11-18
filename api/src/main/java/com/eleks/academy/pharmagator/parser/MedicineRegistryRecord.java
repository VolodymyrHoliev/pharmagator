package com.eleks.academy.pharmagator.parser;

import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

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

    @Parsed(field = "Умови відпуску")
    private String dosageForm;

    @Parsed(field = "Склад (діючі)")
    private String compositionOfActiveSubstances;

    @ParsedCollection(fields = {"Виробник 1: назва українською",
            "Виробник 2: назва українською", "Виробник 3: назва українською", "Виробник 4: назва українською",
            "Виробник 5: назва українською"})
    private List<String> manufacturers;

    @ParsedCollection(fields = {"Виробник 1: країна",
            "Виробник 2: країна", "Виробник 3: країна", "Виробник 4: країна",
            "Виробник 5: країна"})
    private List<String> manufacturersCountries;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineRegistryRecord that = (MedicineRegistryRecord) o;
        return Objects.equals(title, that.title) && Objects.equals(registrationId, that.registrationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, registrationId);
    }
}
