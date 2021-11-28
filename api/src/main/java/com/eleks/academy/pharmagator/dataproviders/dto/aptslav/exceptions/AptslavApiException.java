package com.eleks.academy.pharmagator.dataproviders.dto.aptslav.exceptions;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Getter
public class AptslavApiException extends RuntimeException {

    private final WebClientResponseException cause;

    public AptslavApiException(WebClientResponseException cause) {
        this.cause = cause;
    }

    @SneakyThrows
    @Override
    public String getMessage() {
        return cause.getMessage();
    }

}
