package ru.tinkoff.edu.java.linkparser.github;

import ru.tinkoff.edu.java.linkparser.common.UrlParser;
import ru.tinkoff.edu.java.linkparser.github.payload.GitHubPayload;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Парсер ссылок ресурса github.com, реализация {@link UrlParser}
 */
public final class GitHubUrlParser<T> extends UrlParser<T> {

    public static final Pattern PATTERN_GITHUB_COM_USER_REPO =
            Pattern.compile("^https://([^.]+(?<!github)[.])?github[.]com/(?<user>[^/]+)/(?<repo>[^/]+)");

    private final Result.Processor<T> resultProcessor;

    public GitHubUrlParser(final Result.Processor<T> resultProcessor) {
        this.resultProcessor = Objects.requireNonNull(resultProcessor);
    }

    @Override
    public UrlParser.Result<T> parse(final String url) {

        final var matcher = PATTERN_GITHUB_COM_USER_REPO.matcher(url);

        if (matcher.find()) {
            final var user = matcher.group("user");
            final var repo = matcher.group("repo");

            return Result.success(new GitHubPayload(user, repo), this.resultProcessor);
        }

        return this.parseNext(url);
    }

    public sealed interface Result<T> extends UrlParser.Result<T> {

        record Success<T>(GitHubPayload payload, Processor<T> processor) implements Result<T> {

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public T process() {
                return this.processor.processSuccess(this);
            }
        }

        static <T> Success<T> success(GitHubPayload payload, Processor<T> processor) {
            return new Success<>(payload, processor);
        }

        /**
         * Интерфейс процессора, обрабатывающего результат разбора ссылки
         *
         * @param <T> тип обработки результата работы парсера
         */
        interface Processor<T> {

            T processSuccess(Success<T> result);
        }
    }
}
