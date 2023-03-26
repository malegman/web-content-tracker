package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

import java.util.List;

/**
 * Компонент для рассылки обновления ссылки в указанные телеграмм чаты
 */
@FunctionalInterface
public interface SendUpdatesSpi {

    /**
     * Метод для рассылки обновления ссылки в указанные телеграмм чаты
     *
     * @param linkId      идентификатор ссылки
     * @param url         ссылка
     * @param description описание
     * @param tgChatIds   список идентификаторов чатов телеграмма
     */
    void sendUpdates(LinkId linkId, String url, String description, List<TgChatId> tgChatIds);
}
