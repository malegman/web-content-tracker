package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import org.springframework.http.HttpStatus;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

public final class TrackDataCommandInnerHandler implements CommandInnerHandler {

    private final AddLinkSpi addLinkSpi;

    public TrackDataCommandInnerHandler(final AddLinkSpi addLinkSpi) {
        this.addLinkSpi = Objects.requireNonNull(addLinkSpi);
    }

    @Override
    public Result innerHandle(BotRequest botRequest) {

        final var tgChatId = botRequest.tgChatId();
        return addLinkSpi.addLink(tgChatId, botRequest.messageText())
                .setResultOn2xxSuccessful(Result.sendMessage(tgChatId, "Ссылка успешно добавлена!").success())
                .mapResultOn4xxClientError(status -> {
                    if (status.equals(HttpStatus.CONFLICT)) {
                        return Result.sendMessage(tgChatId, "Эта ссылка уже отслеживается.").abort();
                    } else {
                        return Result.sendMessage(tgChatId, """
                                Проверьте корректность ссылки и введите ссылку ещё раз.
                                Отправьте /exit, чтобы прервать команду.""").repeat();
                    }})
                .setResultOnException(Result.sendMessage(tgChatId, "Попробуйте повторить позже.").abort())
                .getResult();
    }
}