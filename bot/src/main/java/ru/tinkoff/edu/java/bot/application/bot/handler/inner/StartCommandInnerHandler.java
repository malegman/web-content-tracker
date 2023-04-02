package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

public final class StartCommandInnerHandler implements CommandInnerHandler {

    private final WebClient scrapperWebClient;

    public StartCommandInnerHandler(final WebClient scrapperWebClient) {
        this.scrapperWebClient = Objects.requireNonNull(scrapperWebClient);
    }

    @Override
    public Result innerHandle(final TgChatId tgChatId, final Message message) {

        return this.scrapperWebClient.post().uri("/tg-chat/{id}", tgChatId.value())
                .exchangeToMono(clientResponse -> Mono.justOrEmpty(
                        this.handleStatusCode(tgChatId, clientResponse.statusCode())))
                .toFuture()
                .join();
    }

    private Result handleStatusCode(final TgChatId tgChatId, final HttpStatusCode statusCode) {

        if (statusCode.is2xxSuccessful()) {
            return Result.success(new SendMessage(tgChatId.value(), "Привет!"));
        } else if (statusCode.is4xxClientError()) {
            return Result.abort(new SendMessage(tgChatId.value(), "Я уже Вас знаю!"));
        } else if (statusCode.is5xxServerError()) {
            return Result.abort(new SendMessage(tgChatId.value(), "Повторите команду позже."));
        }

        return null;
    }
}