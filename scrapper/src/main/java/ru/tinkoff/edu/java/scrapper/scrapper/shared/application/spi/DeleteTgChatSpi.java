package ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.scrapper.shared.domain.id.TgChatId;

/**
 * Компонент для удаления чата телеграмма
 */
@FunctionalInterface
public interface DeleteTgChatSpi {

    /**
     * Метод для удаления чата телеграмма
     *
     * @param tgChatId идентификатор чата
     */
    void deleteTgChat(TgChatId tgChatId);
}
