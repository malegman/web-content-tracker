package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.web;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.StackOverflowUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.FindQuestionStackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.FindRepoGitHubResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindGitHubUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindStackOverflowUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.StackOverflowQuestionId;
import ru.tinkoff.edu.java.scrapper.application.shared.utils.GitHubUtils;
import ru.tinkoff.edu.java.scrapper.application.shared.utils.StackOverflowUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * Реализация компонента {@link FindGitHubUpdatesSpi} на основе {@link WebClient}
 */
public final class FindStackOverflowUpdatesWebClient implements FindStackOverflowUpdatesSpi {

    private final WebClient stackOverflowWebClient;

    public FindStackOverflowUpdatesWebClient(final WebClient stackOverflowWebClient) {
        this.stackOverflowWebClient = Objects.requireNonNull(stackOverflowWebClient);
    }

    @Override
    public Optional<StackOverflowUpdatesDto> findStackOverflowUpdates(final StackOverflowQuestionId questionId) {

        return this.stackOverflowWebClient.get().uri("/2.3/questions/{questionId}?order=desc&sort=activity&site=stackoverflow", questionId.value())
                .retrieve()
                .bodyToMono(FindQuestionStackOverflowResponse.class)
                .mapNotNull(StackOverflowUtils::dtoFromResponse)
                .blockOptional();
    }
}
