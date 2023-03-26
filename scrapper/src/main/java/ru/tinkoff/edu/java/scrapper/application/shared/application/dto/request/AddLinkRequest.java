package ru.tinkoff.edu.java.scrapper.application.shared.application.dto.request;

/**
 * Принимаемый объект для добавления отслеживаемой ссылки
 *
 * @param url ссылка
 */
public record AddLinkRequest(String url) {
}
