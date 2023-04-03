package ru.tinkoff.edu.java.bot.common.bot;

import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;

public record BotRequest(TgChatId tgChatId, String messageText) {
}
