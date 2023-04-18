package ru.tinkoff.edu.java.linkparser.stackoverflow;

import ru.tinkoff.edu.java.linkparser.common.LinkParser;
import ru.tinkoff.edu.java.linkparser.stackoverflow.domain.id.QuestionId;
import ru.tinkoff.edu.java.linkparser.stackoverflow.payload.StackOverflowPayload;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Парсер ссылок ресурса stackoverflow.com, реализация {@link LinkParser}
 */
public final class StackOverflowLinkParser<T> extends LinkParser<T> {

    public static final Pattern PATTERN_STACKOVERFLOW_QUESTION_ID =
            Pattern.compile("^https://stackoverflow[.]com/questions/(?<questionId>\\d+)");

    private final Result.Processor<T> resultProcessor;

    public StackOverflowLinkParser(final Result.Processor<T> resultProcessor) {
        this.resultProcessor = Objects.requireNonNull(resultProcessor);
    }

    @Override
    public LinkParser.Result<T> parse(final String link) {

        final var matcher = PATTERN_STACKOVERFLOW_QUESTION_ID.matcher(link);

        if (matcher.find()) {
            final var questionId = Long.parseLong(matcher.group("questionId"));

            return Result.success(new StackOverflowPayload(new QuestionId(questionId)), this.resultProcessor);
        }

        return this.parseNext(link);
    }

    public sealed interface Result<T> extends LinkParser.Result<T> {

        record Success<T>(StackOverflowPayload payload, Processor<T> processor) implements Result<T> {

            @Override
            public void visit(Visitor<T> visitor) {
                visitor.setValue(this.processor.processSuccess(this));
            }

            @Override
            public boolean isFailed() {
                return false;
            }
        }

        static <T> Success<T> success(StackOverflowPayload payload, Processor<T> processor) {
            return new Success<>(payload, processor);
        }

        /**
         * Интерфейс процессора, обрабатывающего результат разбора ссылки
         */
        interface Processor<T> {

            T processSuccess(Success<T> result);
        }
    }
}
