package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import org.springframework.http.HttpStatus;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.DeleteLinkSpi;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

public final class UnTrackDataCommandInnerHandler implements CommandInnerHandler {

    private final DeleteLinkSpi deleteLinkSpi;

    public UnTrackDataCommandInnerHandler(final DeleteLinkSpi deleteLinkSpi) {
        this.deleteLinkSpi = Objects.requireNonNull(deleteLinkSpi);
    }

    @Override
    public Result innerHandle(BotRequest botRequest) {

        final var tgChatId = botRequest.tgChatId();
        return deleteLinkSpi.deleteLink(tgChatId, botRequest.messageText())
                .setResultOn2xxSuccessful(Result.sendMessage(tgChatId, "Ссылка успешно удалена!").success())
                .mapResultOn4xxClientError(status -> {
                    if (status.equals(HttpStatus.NOT_FOUND)) {
                        return Result.sendMessage(tgChatId, "Эта ссылка не отслеживается.").abort();
                    } else {
                        return Result.sendMessage(tgChatId, """
                                Проверьте корректность ссылки и введите ссылку ещё раз.
                                Отправьте /exit, чтобы прервать команду.""").repeat();
                    }})
                .setResultOnException(Result.sendMessage(tgChatId, "Попробуйте повторить позже.").abort())
                .getResult();
    }
}
