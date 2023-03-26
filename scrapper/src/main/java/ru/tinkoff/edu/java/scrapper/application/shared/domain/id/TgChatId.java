package ru.tinkoff.edu.java.scrapper.application.shared.domain.id;

/**
 * Идентификатор чата телеграмма
 */
public record TgChatId(long value) {

    public static TgChatId valueOf(String value) {
        return value == null ? null : new TgChatId(Long.parseLong(value));
    }
}
