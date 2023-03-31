package ru.tinkoff.edu.java.bot.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.matcher.AssertionMatcher;
import ru.tinkoff.edu.java.bot.common.errors.ApiErrorResponse;

import java.util.Objects;

public class ApiErrorResponseAssertionMatcher extends AssertionMatcher<String> {

    private final ObjectMapper objectMapper;

    private ApiErrorResponse apiErrorResponse;

    public ApiErrorResponseAssertionMatcher(final ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    public ApiErrorResponse getApiErrorResponse() {
        return this.apiErrorResponse;
    }

    @Override
    public void assertion(String actual) throws AssertionError {

        try {
            this.apiErrorResponse = this.objectMapper.readValue(actual, ApiErrorResponse.class);
        } catch (JsonProcessingException ignored) {
        }
    }
}
