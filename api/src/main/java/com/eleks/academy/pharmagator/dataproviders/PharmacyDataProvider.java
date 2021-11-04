package com.eleks.academy.pharmagator.dataproviders;

import com.eleks.academy.pharmagator.entities.Pharmacy;

/**
 * Due to database constraints we can`t save the data,obtained from the data provider
 *
 * unless we get a proper instance of Pharmacy.
 *
 * The implementations of this interface can return instance of {@link Pharmacy}
 *
 * so we could save data properly in our scheduler.
 *
 * If you're intending to use this approach you have to implement
 *
 * your database persistence logic in {@link com.eleks.academy.pharmagator.scheduler.PharmacyScheduler}
 *
 */
public interface PharmacyDataProvider extends DataProvider {

    /**
     * As long as {@link com.eleks.academy.pharmagator.scheduler.PharmacyScheduler}
     *
     * doesn't have any validation logic you have to
     *
     * make sure that your implementation of this method returns
     *
     * valid {@link Pharmacy} instance which corresponds to your data provider
     *
     * @return corresponding {@link Pharmacy} instance
     */
    Pharmacy getPharmacy();
}
