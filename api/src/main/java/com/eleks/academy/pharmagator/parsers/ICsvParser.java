package com.eleks.academy.pharmagator.parsers;

import java.io.InputStream;
import java.util.stream.Stream;

public interface ICsvParser {

    <T>Stream<T> parse(InputStream inputStream, Class<T> modelType);
}
