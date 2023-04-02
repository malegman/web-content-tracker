package ru.tinkoff.edu.java.bot.application.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.application.bot.handler.inner.HelpCommandInnerHandler;
import ru.tinkoff.edu.java.bot.application.bot.handler.inner.ListCommandInnerHandler;
import ru.tinkoff.edu.java.bot.application.bot.handler.inner.StartCommandInnerHandler;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandHandlerFactory;

@Configuration
public class BotBeans {

    @Bean
    public CommandHandlerFactory startCommandHandlerFactory(WebClient scrapperWebClient) {

        return CommandHandlerFactory.builder()
                .command("/start")
                .addHandlerFunction(new StartCommandInnerHandler(scrapperWebClient))
                .build();
    }

    @Bean
    public CommandHandlerFactory helpCommandHandlerFactory() {

        return CommandHandlerFactory.builder()
                .command("/help")
                .addHandlerFunction(new HelpCommandInnerHandler())
                .build();
    }

    @Bean
    public CommandHandlerFactory listCommandHandlerFactory(WebClient scrapperWebClient) {

        return CommandHandlerFactory.builder()
                .command("/list")
                .addHandlerFunction(new ListCommandInnerHandler(scrapperWebClient))
                .build();
    }
}
