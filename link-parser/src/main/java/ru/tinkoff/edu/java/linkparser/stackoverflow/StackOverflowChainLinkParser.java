package ru.tinkoff.edu.java.linkparser.stackoverflow;

import ru.tinkoff.edu.java.linkparser.common.AbstractChainLinkParser;
import ru.tinkoff.edu.java.linkparser.stackoverflow.payload.StackOverflowPayload;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Парсер ссылок ресурса stackoverflow.com
 */
public final class StackOverflowChainLinkParser<T> extends AbstractChainLinkParser {

    public static final Pattern PATTERN_STACKOVERFLOW_QUESTION_ID =
            Pattern.compile("^[a-z]+://stackoverflow[.]com/questions/(?<questionId>\\d+)");

    private final Result.Processor<T, StackOverflowPayload> resultProcessor;

    public StackOverflowChainLinkParser(final Result.Processor<T, StackOverflowPayload> resultProcessor) {
        this.resultProcessor = Objects.requireNonNull(resultProcessor);
    }

    @Override
    public Result doInternalParse(final String link) {

        final var matcher = PATTERN_STACKOVERFLOW_QUESTION_ID.matcher(link);

        if (matcher.find()) {
            final var questionId = Long.parseLong(matcher.group("questionId"));

            return Result.success(new StackOverflowPayload(questionId), this.resultProcessor);
        }

        return Result.failed();
    }
}
