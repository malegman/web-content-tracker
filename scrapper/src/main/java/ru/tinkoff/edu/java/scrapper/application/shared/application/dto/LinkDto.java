package ru.tinkoff.edu.java.scrapper.application.shared.application.dto;

import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;

/**
 * DTO для получения отслеживаемой ссылки
 *
 * @param id  идентификатор ссылки
 * @param url ссылка
 */
public record LinkDto(LinkId id, String url) {
}
