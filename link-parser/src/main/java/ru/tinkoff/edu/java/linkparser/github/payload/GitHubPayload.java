package ru.tinkoff.edu.java.linkparser.github.payload;

/**
 * Полезная нагрузка, извлекаемая из ссылки вида {@code "github.com/{user}/{repo}"}
 *
 * @param user пользователь сервиса GitHub
 * @param repo наименование репозитория пользователя
 */
public record GitHubPayload(String user, String repo) {
}
