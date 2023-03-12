package ru.tinkoff.edu.java.linkparser.github;

import ru.tinkoff.edu.java.linkparser.common.AbstractChainLinkParser;
import ru.tinkoff.edu.java.linkparser.github.payload.GitHubPayload;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Парсер ссылок ресурса github.com
 */
public final class GitHubChainLinkParser<T> extends AbstractChainLinkParser {

    public static final Pattern PATTERN_GITHUB_COM_USER_REPO =
            Pattern.compile("^[a-z]+://([^.]+(?<!github)[.])?github[.]com/(?<user>[^/]+)/(?<repo>[^/]+)");

    private final Result.Processor<T, GitHubPayload> resultProcessor;

    public GitHubChainLinkParser(final Result.Processor<T, GitHubPayload> resultProcessor) {
        this.resultProcessor = Objects.requireNonNull(resultProcessor);
    }

    @Override
    public Result doInternalParse(final String link) {

        final var matcher = PATTERN_GITHUB_COM_USER_REPO.matcher(link);

        if (matcher.find()) {
            final var user = matcher.group("user");
            final var repo = matcher.group("repo");

            return Result.success(new GitHubPayload(user, repo), this.resultProcessor);
        }

        return Result.failed();
    }
}
