package ru.tinkoff.edu.java.scrapper.application.shared.domain.id;

/**
 * Идентификатор отслеживаемой ссылки чата
 */
public record TgChatLinkId(long value) {

    public static TgChatLinkId valueOf(Long value) {
        return value == null ? null : new TgChatLinkId(value);
    }
}
