package ru.tinkoff.edu.java.scrapper.application.shared.application.dto;

import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.GitHubRepoId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.GitHubUserId;

import java.net.URI;
import java.time.OffsetDateTime;

/**
 * DTO для получения обновлений из github
 */
public record GitHubUpdatesDto(GitHubRepoId id, String repoName, Owner owner, OffsetDateTime updatedAt,
                               Integer watchersCount, Integer size) {

    public record Owner(String login, GitHubUserId id, URI url) {
    }
}
