package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient gitHubWebClient() {
        return WebClient.create("https://api.github.com");
    }

    @Bean
    public WebClient stackOverflowWebClient() {
        return WebClient.create("https://api.stackexchange.com");
    }
}
