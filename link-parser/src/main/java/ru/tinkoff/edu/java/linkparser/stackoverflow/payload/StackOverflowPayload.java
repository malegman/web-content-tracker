package ru.tinkoff.edu.java.linkparser.stackoverflow.payload;

import ru.tinkoff.edu.java.linkparser.stackoverflow.domain.id.QuestionId;

/**
 * Полезная нагрузка, извлекаемая из ссылки вида {@code "stackoverflow.com/questions/{questionId}"}
 *
 * @param questionId идентификатор вопроса
 */
public record StackOverflowPayload(QuestionId questionId) {
}
