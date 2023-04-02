package ru.tinkoff.edu.java.bot.application.shared.application.spi.web;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.LinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.util.Objects;

public final class AddLinkScrapperWebClient implements AddLinkSpi {

    private final WebClient scrapperWebClient;

    public AddLinkScrapperWebClient(final WebClient scrapperWebClient) {
        this.scrapperWebClient = Objects.requireNonNull(scrapperWebClient);
    }

    @Override
    public WebClientBodyResponse<LinkScrapperResponse> addLink(TgChatId tgChatId, String link) {
        return null;
    }
}
