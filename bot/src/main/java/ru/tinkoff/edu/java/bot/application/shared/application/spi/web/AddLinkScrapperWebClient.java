package ru.tinkoff.edu.java.bot.application.shared.application.spi.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.request.AddLinkScrapperRequest;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.LinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.ScrapperHeaders;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.util.Objects;
import java.util.concurrent.CompletionException;

/**
 * Реализация {@link AddLinkSpi} с обращением к scrapper через web-client
 */
@Slf4j
public final class AddLinkScrapperWebClient implements AddLinkSpi {

    private final WebClient scrapperWebClient;

    public AddLinkScrapperWebClient(final WebClient scrapperWebClient) {
        this.scrapperWebClient = Objects.requireNonNull(scrapperWebClient);
    }

    @Override
    public WebClientBodyResponse<LinkScrapperResponse> addLink(TgChatId tgChatId, String link) {
        try {
            return this.scrapperWebClient.post()
                    .uri("/links")
                    .header(ScrapperHeaders.TG_CHAT_ID, String.valueOf(tgChatId.value()))
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(new AddLinkScrapperRequest(link)), AddLinkScrapperRequest.class)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, ClientResponse::createException)
                    .toEntity(LinkScrapperResponse.class)
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
