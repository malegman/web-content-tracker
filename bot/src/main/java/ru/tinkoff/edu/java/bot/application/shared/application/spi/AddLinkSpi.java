package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.LinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

/**
 * Компонент для добавления отслеживаемой ссылки.
 */
@FunctionalInterface
public interface AddLinkSpi {

    /**
     * Метод для добавления отслеживаемой ссылки.
     *
     * @param tgChatId идентификатор чата
     * @param link ссылка для отслеживания
     *
     * @return обертка ответа web-клиента
     */
    WebClientBodyResponse<LinkScrapperResponse> addLink(TgChatId tgChatId, String link);
}
