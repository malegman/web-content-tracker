package ru.tinkoff.edu.java.bot.application.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddChatSpi;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.DeleteLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.FindListLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.AddChatScrapperWebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.AddLinkScrapperWebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.DeleteLinkScrapperWebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.web.FindListLinksScrapperWebClient;

@Configuration
public class BotBeans {

    @Bean
    public AddChatSpi addChatSpi(WebClient scrapperWebClient) {
        return new AddChatScrapperWebClient(scrapperWebClient);
    }

    @Bean
    public FindListLinkSpi findListLinkSpi(WebClient scrapperWebClient) {
        return new FindListLinksScrapperWebClient(scrapperWebClient);
    }

    @Bean
    public AddLinkSpi addLinkSpi(WebClient scrapperWebClient) {
        return new AddLinkScrapperWebClient(scrapperWebClient);
    }

    @Bean
    public DeleteLinkSpi deleteLinkSpi(WebClient scrapperWebClient) {
        return new DeleteLinkScrapperWebClient(scrapperWebClient);
    }
}
