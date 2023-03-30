package ru.tinkoff.edu.java.bot.common.api.invocation;

import ru.tinkoff.edu.java.bot.common.api.validation.SelfValidating;
import ru.tinkoff.edu.java.bot.common.api.validation.Validation;

import java.util.function.Consumer;

/**
 * Обобщенный класс, описывающий операцию
 *
 * @param <P> класс, описывающий полезную нагрузку операции
 * @param <B> класс-строитель полезной нагрузки
 * @param <R> класс, описывающий результат операции
 * @param <V> класс-посетитель результата операции
 */
public abstract class Invocation<P extends Invocation.Payload, B extends Invocation.Payload.Builder<P>,
        R extends Invocation.Result<R, V>, V> {

    /**
     * Метод для выполнения операции
     *
     * @param payloadBuilderCustomizer объект, заполняющий полезную нагрузку
     *
     * @return результат операции
     */
    public R invoke(Consumer<B> payloadBuilderCustomizer) {
        final var builder = this.newPayloadBuilder();
        payloadBuilderCustomizer.accept(builder);

        return this.invoke(builder.build());
    }

    /**
     * Метод для выполнения операции
     *
     * @param payload полезная нагрузка
     *
     * @return результат операции
     */
    public R invoke(P payload) {
        final var validation = payload.validate();
        if (validation.hasErrors()) {
            return this.newValidationFailed(validation);
        }

        return this.invokeInternal(payload);
    }

    /**
     * Метод для реализации операции
     *
     * @param payload полезная нагрузка
     *
     * @return результат операции
     */
    protected abstract R invokeInternal(P payload);

    /**
     * Метод для получения результата операции с ошибкой валидации
     *
     * @param validation объект валидации, содержащий ошибки
     *
     * @return результат операции
     */
    protected abstract R newValidationFailed(Validation validation);

    /**
     * Метод для получения строителя полезной нагрузки
     *
     * @return строитель полезной нагрузки операции
     */
    protected abstract B newPayloadBuilder();

    /**
     * Интерфейс, описывающий полезную нагрузку операции
     */
    public interface Payload extends SelfValidating {

        /**
         * Интерфейс, описывающий строителя полезной нагрузки
         *
         * @param <P> тип полезной нагрузки
         */
        interface Builder<P extends Payload> {

            /**
             * Метод для построения полезной нагрузки
             *
             * @return полезная нагрузка операции
             */
            P build();
        }
    }

    /**
     * Интерфейс, описывающий результат операции
     *
     * @param <R> класс-результат, реализующий данный интерфейс
     * @param <V> класс-посетитель, обрабатывающий результат операции
     */
    public interface Result<R extends Result<R, V>, V> {

        /**
         * Проверяет, является ли результат провальным
         *
         * @return true, если результат является ошибкой
         */
        default boolean isFailed() {
            return true;
        }

        /**
         * Выполняет действие, если результат провальный
         *
         * @param runnable действие
         *
         * @return данный результат
         */
        default Result<R, V> onFailed(final Runnable runnable) {
            if (this.isFailed()) {
                runnable.run();
            }
            return this;
        }

        /**
         * Метод для обработки результата операции
         *
         * @param visitor посетитель результата операции
         */
        void visit(V visitor);
    }
}
