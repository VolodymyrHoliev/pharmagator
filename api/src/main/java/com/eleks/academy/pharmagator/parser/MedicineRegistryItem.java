package com.eleks.academy.pharmagator.parser;

import com.univocity.parsers.annotations.Format;
import com.univocity.parsers.annotations.Parsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRegistryItem {

    @Parsed(field = "Торгівельне найменування")
    private String title;

    @Parsed(field = "Номер Реєстраційного посвідчення")
    private String registrationId;

    @Parsed(field = "Дата початку дії")
    @Format(formats = "dd.MM.yyyy")
    private Date licensedAt;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineRegistryItem that = (MedicineRegistryItem) o;
        return Objects.equals(title, that.title) && Objects.equals(registrationId, that.registrationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, registrationId);
    }
}
