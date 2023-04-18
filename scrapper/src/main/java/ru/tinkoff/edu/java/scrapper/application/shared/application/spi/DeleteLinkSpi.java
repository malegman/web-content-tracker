package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

/**
 * Компонент для удаления отслеживаемой ссылки чата телеграмма
 */
@FunctionalInterface
public interface DeleteLinkSpi {

    /**
     * Метод для удаления отслеживаемой ссылки чата телеграмма
     *
     * @param tgChatId идентификатор чата телеграмма
     * @param link     ссылка
     *
     * @return идентификатор удаленной ссылки
     *
     * @throws TgChatNotExistsException     если чат не найден
     * @throws TgChatLinkNotExistsException если чат не имеет данную ссылку
     */
    TgChatLinkId deleteLink(TgChatId tgChatId, String link)
            throws TgChatLinkNotExistsException, TgChatNotExistsException;
}
