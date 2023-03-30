package ru.tinkoff.edu.java.bot.application.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.StartSpi;
import ru.tinkoff.edu.java.bot.common.telegram.bot.RouterCommand;

@Configuration
public class BotBeans {

    @Bean
    public RouterCommand startRouterCommand(StartSpi startSpi) {

        return RouterCommand.Builder
    }
}
