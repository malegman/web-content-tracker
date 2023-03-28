package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient gitHubWebClient(@Value("${github.access_token}") String accessToken) {
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, "application/vnd.github+json");
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                    httpHeaders.add("X-GitHub-Api-Version", "2022-11-28");
                })
                .build();
    }

    @Bean
    public WebClient stackOverflowWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.stackexchange.com")
                .build();
    }
}
