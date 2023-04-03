package ru.tinkoff.edu.java.bot.application.shared.application.spi.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.ListLinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.FindListLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.ScrapperHeaders;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.util.Objects;

@Slf4j
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
                    .onStatus(HttpStatusCode::is5xxServerError, ClientResponse::createException)
                    .toEntity(ListLinkScrapperResponse.class)
                    .toFuture()
                    .thenApply(WebClientBodyResponse::withBody)
                    .join();
        } catch (Exception e) {
            log.error("Exception", e);
            return WebClientBodyResponse.fromException(e);
        }
    }
}
