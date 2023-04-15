package ru.tinkoff.edu.java.bot.application.shared.application.dto.response;

import java.net.URI;

/**
 * Ответ scrapper с информацией о ссылке
 *
 * @param id   идентификатор ссылки
 * @param link ссылка
 */
public record LinkScrapperResponse(Long id, /* Возможно поменяется на String */ URI link) {
}
