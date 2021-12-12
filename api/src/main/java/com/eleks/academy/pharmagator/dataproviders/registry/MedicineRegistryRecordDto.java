package com.eleks.academy.pharmagator.dataproviders.registry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineRegistryRecordDto {

    private String registrationId;

    private String title;

    private String dosageForm;

    private String manufacturer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicineRegistryRecordDto that = (MedicineRegistryRecordDto) o;
        return Objects.equals(registrationId, that.registrationId) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationId, title);
    }

}
