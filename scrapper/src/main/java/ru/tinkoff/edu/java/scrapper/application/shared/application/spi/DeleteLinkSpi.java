package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

/**
 * Компонент для удаления отслеживаемой ссылки чата телеграмма
 */
@FunctionalInterface
public interface DeleteLinkSpi {

    /**
     * Метод для удаления отслеживаемой ссылки чата телеграмма
     *
     * @param tgChatId идентификатор чата телеграмма
     * @param url      ссылка
     *
     * @return идентификатор удаленной ссылки
     */
    LinkId deleteLink(TgChatId tgChatId, String url);
}
