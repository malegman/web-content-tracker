package ru.tinkoff.edu.java.scrapper.application.shared.application.dto;

import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

/**
 * DTO для получения отслеживаемой ссылки чата
 *
 * @param id  идентификатор ссылки чата
 * @param url ссылка
 */
public record TgChatLinkDto(TgChatLinkId id, String url) {
}
