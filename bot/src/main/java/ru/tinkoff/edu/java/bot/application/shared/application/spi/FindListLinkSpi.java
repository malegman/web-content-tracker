package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.ListLinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

/**
 * Компонент для получения отслеживаемых ссылок.
 */
@FunctionalInterface
public interface FindListLinkSpi {

    /**
     * Метод для получения отслеживаемых ссылок.
     *
     * @param tgChatId идентификатор чата
     *
     * @return обертка ответа web-клиента
     */
    WebClientBodyResponse<ListLinkScrapperResponse> findLinks(TgChatId tgChatId);
}
