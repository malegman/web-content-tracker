package ru.tinkoff.edu.java.bot.application.shared.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.StartSpi;

@Configuration
public class SharedBotBeans {

    @Bean
    public StartSpi startSpi() {
        return tgChatId -> {};
    }
}
