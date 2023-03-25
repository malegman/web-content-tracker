package ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.scrapper.shared.domain.id.TgChatId;

/**
 * Компонент для регистрации чата телеграмма
 */
@FunctionalInterface
public interface RegisterTgChatSpi {

    /**
     * Метод для регистрации чата телеграмма
     *
     * @param tgChatId идентификатор чата
     */
    void registerTgChat(TgChatId tgChatId);
}
