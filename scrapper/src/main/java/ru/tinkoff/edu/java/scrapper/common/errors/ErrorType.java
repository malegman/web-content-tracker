package ru.tinkoff.edu.java.scrapper.common.errors;

import lombok.Getter;

@Getter
public enum ErrorType {

    VALIDATION_FAILED("error.validation_failed.description", "VALIDATION_FAILED"),

    ILLEGAL_ARGUMENTS("error.illegal_arguments.description", "ILLEGAL_ARGUMENTS"),
    EXECUTION_FAILED("error.execution_failed.description", "EXECUTION_FAILED");

    private final String description;
    private final String code;

    ErrorType(final String description, final String code) {
        this.description = description;
        this.code = code;
    }
}
