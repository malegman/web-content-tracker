package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.DeleteLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

public final class UnTrackDataCommandInnerHandler implements CommandInnerHandler {

    private final DeleteLinkSpi deleteLinkSpi;

    public UnTrackDataCommandInnerHandler(final DeleteLinkSpi deleteLinkSpi) {
        this.deleteLinkSpi = Objects.requireNonNull(deleteLinkSpi);
    }

    @Override
    public Result innerHandle(TgChatId tgChatId, Message message) {
        return null;
    }
}
