package ru.tinkoff.edu.java.linkparser.common;

/**
 * Абстрактный парсер ссылок
 */
public abstract class UrlParser<T> {

    private UrlParser<T> next;

    @SafeVarargs
    public static <T> UrlParser<T> link(UrlParser<T> first, UrlParser<T>... chain) {

        UrlParser<T> head = first;

        for (UrlParser<T> nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }

        return first;
    }

    protected abstract Result<T> parse(String link);

    protected Result<T> parseNext(final String url) {

        if (this.next == null) {
            return Result.failed();
        }

        return this.next.parse(url);
    }

    @FunctionalInterface
    public interface Result<T> {

        default boolean isFailed() {
            return true;
        }

        T process();

        @SuppressWarnings("unchecked")
        static <R extends Result<T>, T> R failed() {
            return (R) (Result<T>) () -> null;
        }
    }
}
