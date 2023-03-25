package ru.tinkoff.edu.java.scrapper.scrapper.shared.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.FindLinksSpi;

import java.util.List;

@Configuration
public class SharedLinksBeans {

    @Bean
    public FindLinksSpi findLinksSpi() {
        // TODO реализация на данный момент неизвестна
        return tgChatId -> List.of();
    }

    @Bean
    public AddLinkSpi addLinkSpi() {
        // TODO реализация на данный момент неизвестна
        return (tgChatId, url) -> null;
    }
}
