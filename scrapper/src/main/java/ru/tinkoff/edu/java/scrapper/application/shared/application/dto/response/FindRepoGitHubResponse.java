package ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response;

import java.time.OffsetDateTime;

/**
 * Ответ, который возвращает github на запрос на получение информации о репозитории
 */
public record FindRepoGitHubResponse(Long id, String name, Owner owner, OffsetDateTime updated_at,
                                     Integer watchers_count, Integer size) {

    public record Owner(String login, Long id, String url) {
    }
}
