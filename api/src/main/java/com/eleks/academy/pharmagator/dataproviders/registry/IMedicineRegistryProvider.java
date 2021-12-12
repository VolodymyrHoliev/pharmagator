package com.eleks.academy.pharmagator.dataproviders.registry;

import java.util.stream.Stream;

public interface IMedicineRegistryProvider {

    Stream<MedicineRegistryRecordDto> fetchRecordsByRegistrationId(String registrationId);

}
