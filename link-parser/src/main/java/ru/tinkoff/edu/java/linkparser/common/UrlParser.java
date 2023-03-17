package ru.tinkoff.edu.java.linkparser.common;

/**
 * Абстрактный парсер ссылок, собираемый в цепочку парсеров
 *
 * @param <T> тип обработки результата работы парсера
 */
public abstract class UrlParser<T> {

    private UrlParser<T> next;

    /**
     * Формирует цепочку парсеров
     *
     * @param first первый парсер
     * @param chain последующие парсеры
     *
     * @return парсер, состоящий из цепочки парсеров
     *
     * @param <T> тип обработки результата работы парсера
     */
    @SafeVarargs
    public static <T> UrlParser<T> link(UrlParser<T> first, UrlParser<T>... chain) {

        UrlParser<T> head = first;

        for (UrlParser<T> nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }

        return first;
    }

    /**
     * Метод для разбора ссылки, реализуемый определенными парсерами
     *
     * @param url ссылка, которую необходимо разобрать
     *
     * @return результат разбора ссылки
     */
    protected abstract Result<T> parse(String url);

    /**
     * Метод, вызываемый при разборе строки, для вызова следующего по цепочке парсера
     *
     * @param url ссылка, которую необходимо разобрать
     *
     * @return результат разбора ссылки
     */
    protected Result<T> parseNext(final String url) {

        if (this.next == null) {
            return Result.failed();
        }

        return this.next.parse(url);
    }

    /**
     * Интерфейс результата работы парсера
     *
     * @param <T> тип обработки результата работы парсера
     */
    @FunctionalInterface
    public interface Result<T> {

        /**
         * Проверяет успешность результата
         *
         * @return true, если результат неуспешный
         */
        default boolean isFailed() {
            return true;
        }

        /**
         * Метод для обработки результата парсера
         *
         * @return объект, полученный в результате обработки результата
         */
        T process();

        /**
         * Возвращает проваленный результат (результат - заглушка)
         *
         * @return результат
         * @param <R> класс-реализация интерфейса результата
         * @param <T> тип обработки результата работы парсера
         */
        @SuppressWarnings("unchecked")
        static <R extends Result<T>, T> R failed() {
            return (R) (Result<T>) () -> null;
        }
    }
}
