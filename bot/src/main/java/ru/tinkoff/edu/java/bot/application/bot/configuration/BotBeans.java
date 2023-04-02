package ru.tinkoff.edu.java.bot.application.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.application.bot.handler.inner.*;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.AddChatScrapperWebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.AddLinkScrapperWebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.DeleteLinkScrapperWebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.FindListLinksScrapperWebClient;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandHandlerFactory;

@Configuration
public class BotBeans {

    @Bean
    public CommandHandlerFactory startCommandHandlerFactory(WebClient scrapperWebClient) {

        return CommandHandlerFactory.builder()
                .command("/start")
                .addHandlerFunction(new StartCommandInnerHandler(
                        new AddChatScrapperWebClient(scrapperWebClient)))
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
                .addHandlerFunction(new ListCommandInnerHandler(
                        new FindListLinksScrapperWebClient(scrapperWebClient)))
                .build();
    }

    @Bean
    public CommandHandlerFactory trackCommandHandlerFactory(WebClient scrapperWebClient) {

        return CommandHandlerFactory.builder()
                .command("/track")
                .addHandlerFunction(new TrackGreetingCommandInnerFunction())
                .addHandlerFunction(new TrackDataCommandInnerHandler(
                        new AddLinkScrapperWebClient(scrapperWebClient)))
                .build();
    }

    @Bean
    public CommandHandlerFactory unTrackCommandHandlerFactory(WebClient scrapperWebClient) {

        return CommandHandlerFactory.builder()
                .command("/untrack")
                .addHandlerFunction(new UnTrackGreetingCommandInnerFunction())
                .addHandlerFunction(new UnTrackDataCommandInnerHandler(
                        new DeleteLinkScrapperWebClient(scrapperWebClient)))
                .build();
    }
}
