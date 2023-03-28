package ru.tinkoff.edu.java.scrapper.application.shared.utils;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.GitHubUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.FindRepoGitHubResponse;

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

        return new GitHubUpdatesDto();
    }
}
