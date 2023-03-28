package ru.tinkoff.edu.java.scrapper.application.shared.utils;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.StackOverflowUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.FindQuestionStackOverflowResponse;

import java.util.Objects;

/**
 * Вспомогательные средства для компонентов stackoverflow
 */
public class StackOverflowUtils {

    /**
     * Преобразует {@link FindQuestionStackOverflowResponse} в {@link StackOverflowUpdatesDto}
     */
    public static StackOverflowUpdatesDto dtoFromResponse(final FindQuestionStackOverflowResponse response) {

        if (Objects.isNull(response)) {
            return null;
        }

        return new StackOverflowUpdatesDto();
    }
}
