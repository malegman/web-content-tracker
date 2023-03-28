package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.web;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.GitHubUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindGitHubUpdatesSpi;

import java.util.Objects;
import java.util.Optional;

/**
 * Реализация компонента {@link FindGitHubUpdatesSpi} на основе {@link WebClient}
 */
public final class FindGitHubUpdatesWebClient implements FindGitHubUpdatesSpi {

    private final WebClient gitHubWebClient;

    public FindGitHubUpdatesWebClient(final WebClient gitHubWebClient) {
        this.gitHubWebClient = Objects.requireNonNull(gitHubWebClient);
    }

    @Override
    public Optional<GitHubUpdatesDto> findGitHubUpdates(final String username, final String repo) {

        return Optional.empty();
    }
}
