package ru.tinkoff.edu.java.scrapper.application.shared.application.dto.request;

/**
 * Принимаемый объект для удаления отслеживаемой ссылки
 *
 * @param url ссылка
 */
public record RemoveLinkRequest(String url) {
}
