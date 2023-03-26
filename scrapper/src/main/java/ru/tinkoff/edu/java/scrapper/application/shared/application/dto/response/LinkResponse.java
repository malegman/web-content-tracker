package ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response;

/**
 * Возвращаемый объект отслеживаемой ссылки
 *
 * @param id  идентификатор ссылки
 * @param url ссылка
 */
public record LinkResponse(Long id, String url) {
}
