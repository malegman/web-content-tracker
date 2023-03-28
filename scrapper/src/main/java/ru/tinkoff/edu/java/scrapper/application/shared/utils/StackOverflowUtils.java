package ru.tinkoff.edu.java.scrapper.application.shared.utils;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.StackOverflowUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.FindQuestionStackOverflowResponse;

import java.time.Instant;
import java.time.OffsetDateTime;
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

        final var owner = response.owner();
        return new StackOverflowUpdatesDto(
                response.tags(),
                new StackOverflowUpdatesDto.Owner(
                        owner.account_id(),
                        owner.user_id(),
                        owner.display_name(),
                        owner.link()),
                response.is_answered(),
                OffsetDateTime.from(Instant.ofEpochMilli(response.last_activity_date())),
                OffsetDateTime.from(Instant.ofEpochMilli(response.last_edit_date())));
    }
}
