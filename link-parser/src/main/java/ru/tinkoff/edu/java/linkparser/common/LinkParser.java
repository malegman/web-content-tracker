package ru.tinkoff.edu.java.linkparser.common;

public interface LinkParser {

    Result doParse(final String link);

    sealed interface Result {

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
