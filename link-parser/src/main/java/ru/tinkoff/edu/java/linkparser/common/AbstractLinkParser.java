package ru.tinkoff.edu.java.linkparser.common;

import java.util.Objects;

/**
 * Абстрактный парсер ссылок
 */
public abstract class AbstractLinkParser {

    private AbstractLinkParser next;

    protected abstract Result doInternalParse(String link);

    public void setNext(final AbstractLinkParser next) {
        this.next = Objects.requireNonNull(next);
    }

    public Result doParse(final String link) {

        final var result = this.doInternalParse(link);
        if (result.isSuccess()) {
            return result;
        }

        if (this.next == null) {
            return Result.failed();
        } else {
            return this.next.doParse(link);
        }
    }

    public sealed interface Result {

        default boolean isSuccess() {
            return false;
        }

        <T> T process();

        static <P, T> Success<P, T> success(P payload, Processor<T, P> processor) {
            return new Success<>(payload, processor);
        }

        record Success<P, T>(P payload, Processor<T, P> processor) implements Result {

            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public T process() {
                return this.processor.processSuccess(this);
            }
        }

        static Failed failed() {
            return Failed.INSTANCE;
        }

        enum Failed implements Result {
            INSTANCE;

            @Override
            public <T> T process() {
                return null;
            }
        }

        interface Processor<T, P> {

            T processSuccess(Success<P, T> result);
        }
    }
}
