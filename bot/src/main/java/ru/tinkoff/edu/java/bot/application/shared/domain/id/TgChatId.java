package ru.tinkoff.edu.java.bot.application.shared.domain.id;

/**
 * Идентификатор чата телеграмма
 */
public record TgChatId(long value) {

    public static TgChatId valueOf(Long value) {
        return value == null ? null : new TgChatId(value);
    }
}
