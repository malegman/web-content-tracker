package ru.tinkoff.edu.java.bot.application.shared.application.dto.request;

import java.util.List;

/**
 * Принимаемый объект для рассылки обновления ссылки в указанные телеграмм чаты
 *
 * @param id          идентификатор ссылки
 * @param url         ссылка
 * @param description описание
 * @param tgChatIds   список идентификаторов чатов телеграмма
 */
public record LinkUpdateRequest(Long id, String url, String description, List<Long> tgChatIds) {
}
