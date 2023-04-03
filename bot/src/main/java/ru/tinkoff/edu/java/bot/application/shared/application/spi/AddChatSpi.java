package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

/**
 * Компонент для добавления чата телеграм.
 */
@FunctionalInterface
public interface AddChatSpi {

    /**
     * Метод для добавления чата телеграм.
     *
     * @param tgChatId идентификатор чата
     *
     * @return обертка ответа web-клиента
     */
    WebClientBodyResponse<Void> addChat(TgChatId tgChatId);
}
