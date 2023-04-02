package ru.tinkoff.edu.java.bot.application.shared.application.spi.web;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddChatSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.util.Objects;

public final class AddChatScrapperWebClient implements AddChatSpi {

    private final WebClient scrapperWebClient;

    public AddChatScrapperWebClient(final WebClient scrapperWebClient) {
        this.scrapperWebClient = Objects.requireNonNull(scrapperWebClient);
    }

    @Override
    public WebClientBodyResponse<Void> addChat(TgChatId tgChatId) {
        try {
            return this.scrapperWebClient.post().uri("/tg-chat/{id}", tgChatId.value())
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createException)
                    .toBodilessEntity()
                    .toFuture()
                    .thenApply(WebClientBodyResponse::bodiless)
                    .join();
        } catch (Exception e) {
            return WebClientBodyResponse.fromException(e);
        }
    }
}
