package ru.tinkoff.edu.java.bot.common.validation;

import java.util.*;

/**
 * Результат валидации объектов
 *
 * @param fieldErrors ошибки полей валидируемого объекта
 */
public record Validation(Map<String, List<String>> fieldErrors) {

    public Validation() {
        this(new HashMap<>());
    }

    /**
     * Регистрация ошибки поля
     *
     * @param field поле, содержащее некорректное значение
     * @param error сообщение ошибки для поля
     */
    public void registerFieldError(String field, String error) {
        this.fieldErrors.computeIfAbsent(field, f -> new ArrayList<>()).add(error);
    }

    /**
     * Возвращает ошибки полей в виде неизменяемой коллекции
     *
     * @return ошибки полей
     */
    public Map<String, List<String>> getFieldErrors() {
        return Collections.unmodifiableMap(this.fieldErrors);
    }

    /**
     * Проверка наличия ошибок в валидации
     *
     * @return true, если есть ошибки валидации
     */
    public boolean hasErrors() {
        return !this.fieldErrors.isEmpty();
    }
}
