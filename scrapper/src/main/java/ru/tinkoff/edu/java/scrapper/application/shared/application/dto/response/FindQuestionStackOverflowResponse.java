package ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Ответ, который возвращает stackoverflow на запрос на получение информации о вопросе
 */
public record FindQuestionStackOverflowResponse(List<String> tags, Owner owner, Boolean is_answered,
                                                Long last_activity_date, Long last_edit_date) {

    public record Owner(Long account_id, Long user_id, String display_name, String link) {
    }
}
