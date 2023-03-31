package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.web;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.GitHubUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.FindRepoGitHubResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindGitHubUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.utils.GitHubUtils;

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
    public Optional<GitHubUpdatesDto> findGitHubUpdates(final String owner, final String repo) {

        return this.gitHubWebClient.get().uri("/repos/{owner}/{repo}", owner, repo)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                .bodyToMono(FindRepoGitHubResponse.class)
                .mapNotNull(GitHubUtils::dtoFromResponse)
                .blockOptional();
    }
}
