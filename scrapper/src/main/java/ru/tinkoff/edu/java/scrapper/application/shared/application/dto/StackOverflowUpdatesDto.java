package ru.tinkoff.edu.java.scrapper.application.shared.application.dto;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DTO для получения обновлений из stackoverflow
 */
public record StackOverflowUpdatesDto(List<String> tags, Owner owner, Boolean isAnswered,
                                      OffsetDateTime lastActivityDate, OffsetDateTime lastEditDate) {

    public record Owner(Long accountId, Long userId, String displayName, String link) {
    }
}
