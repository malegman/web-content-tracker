package ru.tinkoff.edu.java.bot.common.validation;

/**
 * Интерфейс самовалидирующегося объекта
 */
@FunctionalInterface
public interface SelfValidating {

    /**
     * Валидирование объекта
     *
     * @return результат валидации
     */
    Validation validate();
}
