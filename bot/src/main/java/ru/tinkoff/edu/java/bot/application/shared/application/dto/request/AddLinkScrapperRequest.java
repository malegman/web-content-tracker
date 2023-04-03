package ru.tinkoff.edu.java.bot.application.shared.application.dto.request;

/**
 * Тело запроса scrapper на запрос добавления ссылки
 *
 * @param link ссылка для добавления
 */
public record AddLinkScrapperRequest(String link) {
}
