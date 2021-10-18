package com.eleks.academy.pharmagator.configuration;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class DataProvidersConfig {
    @Value("${pharmagator.data-providers.aptslav.base-url}")
    private String aptslavBaseUrl;

    @SneakyThrows
    @Bean
    public WebClient aptslavWebClient(){
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        HttpClient httpClient = HttpClient
                .create()
                .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT,MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(connector)
                .baseUrl(aptslavBaseUrl)
                .build();
    }


}
