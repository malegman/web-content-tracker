package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

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
