package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import ru.tinkoff.edu.java.bot.application.bot.dto.ListLinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;
import java.util.stream.Collectors;

public final class ListCommandInnerHandler implements CommandInnerHandler {

    private final WebClient scrapperWebClient;

    public ListCommandInnerHandler(final WebClient scrapperWebClient) {
        this.scrapperWebClient = Objects.requireNonNull(scrapperWebClient);
    }
    @Override
    public Result innerHandle(TgChatId tgChatId, Message message) {

        new DefaResponseS

        try {
            return this.scrapperWebClient.get().uri("/links")
                    .header("Tg-Chat-Id", String.valueOf(tgChatId.value()))
                    .ex()
                    .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                    .bodyToMono(ListLinkScrapperResponse.class)
                    .mapNotNull(listLinks -> {
                        if (listLinks == null || listLinks.size() == 0) {
                            return Result.success(new SendMessage(tgChatId.value(), "Я не нашел отслеживаемые ссылки."));
                        } else {
                            final var inlineKeyboard = new InlineKeyboardMarkup();
                            listLinks.links().forEach(link -> inlineKeyboard.addRow(
                                    new InlineKeyboardButton(link.link().toString()).url(link.link().toString())));
                            return Result.success(new SendMessage(tgChatId.value(), "")
                                    .replyMarkup(inlineKeyboard));
                        }
                    })
                    .toFuture()
                    .join();
        } catch (Exception e) {
            return Result.abort(new SendMessage(tgChatId.value(), "Я не нашел отслеживаемые ссылки."));
        }
    }

    private Result handleStatusCode(final TgChatId tgChatId, final ClientResponse clientResponse) {

        final var statusCode = clientResponse.statusCode();

        if (statusCode.is2xxSuccessful()) {

            final var listLinks = clientResponse
                    .bodyToMono(ListLinkScrapperResponse.class)
                    .toFuture()
                    .join();

            return Result.success(new SendMessage(tgChatId.value(),
                    listLinks == null || listLinks.size() == 0
                            ? "Я не нашел отслеживаемые ссылки."
                            : listLinks.links().stream()
                            .map(linkScrapperResponse -> linkScrapperResponse.link().toString())
                            .collect(Collectors.joining("\r\n"))));
        } else {
            return Result.abort(new SendMessage(tgChatId.value(), "Я не нашел отслеживаемые ссылки."));
        }
    }
}
