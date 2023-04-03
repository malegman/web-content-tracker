package ru.tinkoff.edu.java.bot.application.shared.application.spi.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddChatSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.util.Objects;
import java.util.concurrent.CompletionException;

/**
 * Реализация {@link AddChatSpi} с обращением к scrapper через web-client
 */
@Slf4j
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
                    .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                    .toBodilessEntity()
                    .toFuture()
                    .thenApply(WebClientBodyResponse::bodiless)
                    .join();
        } catch (CompletionException e) {
            return WebClientBodyResponse.fromWebClientResponseException((WebClientResponseException) e.getCause());
        } catch (Exception e) {
            log.error("Exception", e);
            return WebClientBodyResponse.fromException(e);
        }
    }
}
