package ru.tinkoff.edu.java.scrapper.scrapper.shared.application.dto.request;

/**
 * Принимаемый объект для удаления отслеживаемой ссылки
 *
 * @param url ссылка
 */
public record RemoveLinkRequest(String url) {
}
