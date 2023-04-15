package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.LinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

/**
 * Компонент для удаления отслеживаемой ссылки.
 */
@FunctionalInterface
public interface DeleteLinkSpi {

    /**
     * Метод для удаления отслеживаемой ссылки.
     *
     * @param tgChatId идентификатор чата
     * @param link ссылка для удаления
     *
     * @return обертка ответа web-клиента
     */
    WebClientBodyResponse<LinkScrapperResponse> deleteLink(TgChatId tgChatId, String link);
}
