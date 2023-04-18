package ru.tinkoff.edu.java.linkparser.common;

/**
 * Абстрактный парсер ссылок, собираемый в цепочку парсеров
 *
 * @param <T> тип обработки результата работы парсера
 */
public abstract class LinkParser<T> {

    private LinkParser<T> next;

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
    public static <T> LinkParser<T> link(LinkParser<T> first, LinkParser<T>... chain) {

        LinkParser<T> head = first;

        for (LinkParser<T> nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }

        return first;
    }

    /**
     * Метод для разбора ссылки, реализуемый определенными парсерами
     *
     * @param link ссылка, которую необходимо разобрать
     *
     * @return результат разбора ссылки
     */
    public abstract Result<T> parse(String link);

    /**
     * Метод, вызываемый при разборе строки, для вызова следующего по цепочке парсера
     *
     * @param link ссылка, которую необходимо разобрать
     *
     * @return результат разбора ссылки
     */
    protected Result<T> parseNext(final String link) {

        if (this.next == null) {
            return Result.failed();
        }

        return this.next.parse(link);
    }

    /**
     * Интерфейс результата работы парсера
     *
     * @param <T> тип обработки результата работы парсера
     */
    @FunctionalInterface
    public interface Result<T> {

        /**
         * Класс-посетитель результата парсера
         * @param <T> тип обработки результата работы парсера
         */
        interface Visitor<T> {

            void setValue(T value);
        }

        /**
         * Метод для посещения результата парсера
         */
        void visit(Visitor<T> visitor);

        /**
         * Проверяет успешность результата
         *
         * @return true, если результат неуспешный
         */
        default boolean isFailed() {
            return true;
        }

        /**
         * Возвращает проваленный результат (результат - заглушка)
         *
         * @return результат
         * @param <T> тип обработки результата работы парсера
         */
        static <T> Result<T> failed() {
            return visitor -> {};
        }
    }
}
