package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import org.springframework.http.HttpStatus;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

/**
 * Обработчик сообщения команды бота /track, реализует {@link CommandInnerHandler}.<br>
 * Обрабатывает добавление ссылки.
 */
public final class TrackDataCommandInnerHandler implements CommandInnerHandler {

    private final AddLinkSpi addLinkSpi;

    public TrackDataCommandInnerHandler(final AddLinkSpi addLinkSpi) {
        this.addLinkSpi = Objects.requireNonNull(addLinkSpi);
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public String getCommand() {
        return "/track";
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
                .setResultOnException(Result.sendMessage(tgChatId, "Что-то пошло не так, повторите позже.").abort())
                .getResult();
    }
}
