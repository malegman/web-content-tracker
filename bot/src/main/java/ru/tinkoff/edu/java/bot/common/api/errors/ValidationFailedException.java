package ru.tinkoff.edu.java.bot.common.api.errors;

import ru.tinkoff.edu.java.bot.common.api.validation.Validation;

import java.util.stream.Collectors;

/**
 * Ошибка валидации
 */
public class ValidationFailedException extends RuntimeException {

    /**
     * Создает исключение на основе объекта валидации с ошибками
     *
     * @param validation объект валидации
     */
    public ValidationFailedException(Validation validation) {

        super(validation.getFieldErrors()
                .entrySet()
                .stream()
                .map(entry -> String.format("field=%s,errors=%s",
                        entry.getKey(),
                        entry.getValue()))
                .collect(Collectors.joining("];[", "[", "]")));
    }
}
