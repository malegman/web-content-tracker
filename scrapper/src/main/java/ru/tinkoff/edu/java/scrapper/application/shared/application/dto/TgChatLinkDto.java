package ru.tinkoff.edu.java.scrapper.application.shared.application.dto;

import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkType;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

/**
 * DTO для получения отслеживаемой ссылки чата
 *
 * @param id       идентификатор ссылки чата
 * @param link     ссылка
 * @param linkType тип ссылки
 */
public record TgChatLinkDto(TgChatLinkId id, String link, LinkType linkType) {
}
