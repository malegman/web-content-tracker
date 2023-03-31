package ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response;

import java.util.List;

/**
 * Возвращаемый объект списка отслеживаемых ссылок
 *
 * @param links список ссылок
 * @param size  размер списка
 */
public record ListLinksResponse(List<LinkResponse> links, Integer size) {
}
