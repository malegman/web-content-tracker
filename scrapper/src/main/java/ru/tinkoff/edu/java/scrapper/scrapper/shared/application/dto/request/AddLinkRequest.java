package ru.tinkoff.edu.java.scrapper.scrapper.shared.application.dto.request;

/**
 * Принимаемый объект для добавления отслеживаемой ссылки
 *
 * @param url ссылка
 */
public record AddLinkRequest(String url) {
}
