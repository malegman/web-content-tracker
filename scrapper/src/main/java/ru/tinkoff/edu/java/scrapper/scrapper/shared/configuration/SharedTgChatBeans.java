package ru.tinkoff.edu.java.scrapper.scrapper.shared.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.DeleteTgChatSpi;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.RegisterTgChatSpi;

@Configuration
public class SharedTgChatBeans {

    @Bean
    public RegisterTgChatSpi registerTgChatSpi() {
        // TODO реализация на данный момент неизвестна
        return tgChatId -> {};
    }

    @Bean
    public DeleteTgChatSpi deleteTgChatSpi() {
        // TODO реализация на данный момент неизвестна
        return tgChatId -> {};
    }
}
