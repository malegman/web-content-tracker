package ru.tinkoff.edu.java.bot.application.shared.domain.id;

/**
 * Идентификатор отслеживаемой ссылки
 */
public record LinkId(long value) {

    public static LinkId valueOf(Long value) {
        return value == null ? null : new LinkId(value);
    }
}
