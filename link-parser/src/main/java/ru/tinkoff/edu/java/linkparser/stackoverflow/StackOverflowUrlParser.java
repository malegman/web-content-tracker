package ru.tinkoff.edu.java.linkparser.stackoverflow;

import ru.tinkoff.edu.java.linkparser.common.UrlParser;
import ru.tinkoff.edu.java.linkparser.stackoverflow.payload.StackOverflowPayload;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Парсер ссылок ресурса stackoverflow.com
 */
public final class StackOverflowUrlParser<T> extends UrlParser<T> {

    public static final Pattern PATTERN_STACKOVERFLOW_QUESTION_ID =
            Pattern.compile("^[a-z]+://stackoverflow[.]com/questions/(?<questionId>\\d+)");

    private final Result.Processor<T> resultProcessor;

    public StackOverflowUrlParser(final Result.Processor<T> resultProcessor) {
        this.resultProcessor = Objects.requireNonNull(resultProcessor);
    }

    @Override
    public UrlParser.Result<T> parse(final String url) {

        final var matcher = PATTERN_STACKOVERFLOW_QUESTION_ID.matcher(url);

        if (matcher.find()) {
            final var questionId = Long.parseLong(matcher.group("questionId"));

            return Result.success(new StackOverflowPayload(questionId), this.resultProcessor);
        }

        return this.parseNext(url);
    }

    public sealed interface Result<T> extends UrlParser.Result<T> {

        record Success<T>(StackOverflowPayload payload, Processor<T> processor) implements Result<T> {

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public T process() {
                return this.processor.processSuccess(this);
            }
        }

        static <T> Success<T> success(StackOverflowPayload payload, Processor<T> processor) {
            return new Success<>(payload, processor);
        }

        interface Processor<T> {

            T processSuccess(Success<T> result);
        }
    }
}
