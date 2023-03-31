package ru.tinkoff.edu.java.bot.application.shared.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.SendUpdatesSpi;

@Configuration
public class SharedLinksBeans {

    @Bean
    public SendUpdatesSpi sendUpdatesSpi() {
        // TODO реализация на данный момент неизвестна
        return (linkId, url, description, tgChatIds) -> {};
    }
}
