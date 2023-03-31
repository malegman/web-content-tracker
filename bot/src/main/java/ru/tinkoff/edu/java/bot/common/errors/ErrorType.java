package ru.tinkoff.edu.java.bot.common.errors;

import lombok.Getter;

@Getter
public enum ErrorType {

    VALIDATION_FAILED("error.validation_failed.description", "VALIDATION_FAILED"),
    EXECUTION_FAILED("error.execution_failed.description", "EXECUTION_FAILED");

    private final String description;
    private final String code;

    ErrorType(final String description, final String code) {
        this.description = description;
        this.code = code;
    }
}
