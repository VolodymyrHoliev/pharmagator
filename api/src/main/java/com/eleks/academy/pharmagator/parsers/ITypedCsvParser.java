package com.eleks.academy.pharmagator.parsers;

import java.io.InputStream;
import java.util.stream.Stream;

/**
 *
 * @param <T> represents type of model we want to obtain
 */
public interface ITypedCsvParser<T> {

    Stream<T> parse(InputStream inputStream);

}
