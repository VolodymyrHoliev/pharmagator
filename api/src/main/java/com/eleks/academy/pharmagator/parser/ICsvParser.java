package com.eleks.academy.pharmagator.parser;

import java.io.InputStream;
import java.util.stream.Stream;

public interface ICsvParser {

    <T>Stream<T> parse(InputStream inputStream, Class<T> modelType);
}
