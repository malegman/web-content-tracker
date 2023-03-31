package ru.tinkoff.edu.java.bot.application.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.application.bot.handler.StartHandlerFunction;
import ru.tinkoff.edu.java.bot.application.bot.usecase.StartUseCase;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.StartSpi;
import ru.tinkoff.edu.java.bot.common.telegram.bot.CommandHandlerFactory;

@Configuration
public class BotBeans {

    @Bean
    public CommandHandlerFactory startCommandHandlerFactory(StartSpi startSpi) {

        return CommandHandlerFactory.builder()
                .command("/start")
                .addHandlerFunction(new StartHandlerFunction(new StartUseCase(startSpi)))
                .build();
    }
}
