package com.eleks.academy.pharmagator.parsers.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation used to mark collections in the DTOs
 * For instance, in our .csv file we can have a few headers which are very similar
 * (e.g 'Manufacturer 1','Manufacturer 2' etc.)
 * Most likely, we would like to save values into one list
 * Check out {@link com.eleks.academy.pharmagator.parsers.csv.MedicineRecordProcessor} to see
 * how it works
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParsedCollection {
    /**
     * Fields represents headers' names from the .csv file
     */
    String[] fields() default {};
}
