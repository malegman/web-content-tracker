package ru.tinkoff.edu.java.scrapper.application.shared.domain.id;

/**
 * Идентификатор отслеживаемой ссылки
 */
public record LinkId(long value) {

    public static Long valueFrom(LinkId linkId) {
        return linkId == null ? null : linkId.value;
    }
}
