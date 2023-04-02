package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
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
        return null;
    }
}
