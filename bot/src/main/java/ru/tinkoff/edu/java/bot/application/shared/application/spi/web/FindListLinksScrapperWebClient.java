package ru.tinkoff.edu.java.bot.application.shared.application.spi.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.ListLinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.FindListLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.ScrapperHeaders;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.util.Objects;

/**
 * Реализация {@link FindListLinkSpi} с обращением к scrapper через web-client
 */
@Slf4j
@Component
public final class FindListLinksScrapperWebClient implements FindListLinkSpi {

    private final WebClient scrapperWebClient;

    public FindListLinksScrapperWebClient(final WebClient scrapperWebClient) {
        this.scrapperWebClient = Objects.requireNonNull(scrapperWebClient);
    }

    @Override
    public WebClientBodyResponse<ListLinkScrapperResponse> findLinks(TgChatId tgChatId) {
        try {
            return this.scrapperWebClient.get().uri("/links")
                    .header(ScrapperHeaders.TG_CHAT_ID, String.valueOf(tgChatId.value()))
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                    .toEntity(ListLinkScrapperResponse.class)
                    .toFuture()
                    .thenApply(WebClientBodyResponse::withBody)
                    .join();
        } catch (Exception e) {
            if (e.getCause() instanceof WebClientResponseException responseException) {
                return WebClientBodyResponse.fromWebClientResponseException(responseException);
            }
            log.error("Exception", e);
            return WebClientBodyResponse.fromException(e);
        }
    }
}
