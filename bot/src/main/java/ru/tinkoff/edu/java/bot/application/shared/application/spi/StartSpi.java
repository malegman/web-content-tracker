package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

@FunctionalInterface
public interface StartSpi {

    void start(TgChatId tgChatId);
}
