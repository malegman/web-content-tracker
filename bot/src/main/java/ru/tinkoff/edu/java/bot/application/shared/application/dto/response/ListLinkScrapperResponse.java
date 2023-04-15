package ru.tinkoff.edu.java.bot.application.shared.application.dto.response;

import java.util.List;

/**
 * Ответ scrapper на запрос для получения отслеживаемых ссылок.
 *
 * @param links список информации о ссылках
 * @param size  размер списка
 */
public record ListLinkScrapperResponse(List<LinkScrapperResponse> links, Integer size) {
}
