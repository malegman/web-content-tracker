package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddChatSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

public final class StartCommandInnerHandler implements CommandInnerHandler {

    private final AddChatSpi addChatSpi;

    public StartCommandInnerHandler(final AddChatSpi addChatSpi) {
        this.addChatSpi = Objects.requireNonNull(addChatSpi);
    }

    @Override
    public Result innerHandle(final TgChatId tgChatId, final Message message) {

        return this.addChatSpi.addChat(tgChatId)
                .setResultOn2xxSuccessful(Result.sendMessage(tgChatId, "Привет!").success())
                .setResultOn4xxClientError(Result.sendMessage(tgChatId, "Я уже Вас знаю!").abort())
                .setResultOnException(Result.sendMessage(tgChatId, "Повторите команду позже.").abort())
                .getResult();
    }
}