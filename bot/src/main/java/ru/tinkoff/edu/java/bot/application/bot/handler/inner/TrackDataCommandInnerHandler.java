package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
import org.springframework.http.HttpStatus;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

public final class TrackDataCommandInnerHandler implements CommandInnerHandler {

    private final AddLinkSpi addLinkSpi;

    public TrackDataCommandInnerHandler(final AddLinkSpi addLinkSpi) {
        this.addLinkSpi = Objects.requireNonNull(addLinkSpi);
    }

    @Override
    public Result innerHandle(TgChatId tgChatId, Message message) {
        return addLinkSpi.addLink(tgChatId, message.text())
                .setResultOn2xxSuccessful(Result.sendMessage(tgChatId, "Ссылка успешно добавлена!").success())
                .mapResultOn4xxClientError(status -> {
                    if (status.equals(HttpStatus.CONFLICT)) {
                        return Result.sendMessage(tgChatId, "Эта ссылка уже отслеживается.").abort();
                    } else {
                        return Result.sendMessage(tgChatId, "Проверьте ссылку и напишите ещё раз.").repeat();
                    }})
                .setResultOnException(Result.sendMessage(tgChatId, "Попробуйте повторить позже.").abort())
                .getResult();
    }
}
