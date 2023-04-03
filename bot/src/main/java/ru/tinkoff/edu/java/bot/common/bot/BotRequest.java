package ru.tinkoff.edu.java.bot.common.bot;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

/**
 * Тело запроса бота.
 *
 * @param tgChatId    идентификатор чата
 * @param messageText текст сообщения
 */
public record BotRequest(TgChatId tgChatId, String messageText) {
}
