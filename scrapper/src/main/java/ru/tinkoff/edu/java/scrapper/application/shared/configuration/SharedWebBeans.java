package ru.tinkoff.edu.java.scrapper.application.shared.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindGitHubUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindStackOverflowUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.web.FindGitHubUpdatesWebClient;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.web.FindStackOverflowUpdatesWebClient;

@Configuration
public class SharedWebBeans {

    @Bean
    public FindGitHubUpdatesSpi findGitHubUpdatesSpi(
            @Qualifier("gitHubWebClient") WebClient gitHubWebClient) {
        return new FindGitHubUpdatesWebClient(gitHubWebClient);
    }

    @Bean
    public FindStackOverflowUpdatesSpi findStackOverflowUpdatesSpi(
            @Qualifier("stackOverflowWebClient") WebClient stackOverflowWebClient) {
        return new FindStackOverflowUpdatesWebClient(stackOverflowWebClient);
    }
}
