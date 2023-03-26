package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

/**
 * Компонент для добавления отслеживаемой ссылки в чат телеграмма
 */
@FunctionalInterface
public interface AddLinkSpi {

    /**
     * Метод для добавления отслеживаемой ссылки в чат телеграмма
     *
     * @param tgChatId идентификатор чата
     * @param url      ссылка
     *
     * @return идентификатор добавленной ссылки
     */
    LinkId addLink(TgChatId tgChatId, String url);
}
