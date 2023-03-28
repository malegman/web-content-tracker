package ru.tinkoff.edu.java.scrapper.application.shared.utils;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.GitHubUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.FindRepoGitHubResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.GitHubRepoId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.GitHubUserId;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Вспомогательные средства для компонентов github
 */
public class GitHubUtils {

    /**
     * Преобразует {@link FindRepoGitHubResponse} в {@link GitHubUpdatesDto}
     */
    public static GitHubUpdatesDto dtoFromResponse(final FindRepoGitHubResponse response) {

        if (Objects.isNull(response)) {
            return null;
        }

        final var owner = response.owner();

        return new GitHubUpdatesDto(
                GitHubRepoId.valueOf(response.id()),
                response.name(),
                new GitHubUpdatesDto.Owner(
                        owner.login(),
                        GitHubUserId.valueOf(owner.id()),
                        URI.create(owner.url())),
                OffsetDateTime.from(Instant.ofEpochMilli(response.updated_at())),
                response.watchers_count(),
                response.size());
    }
}
