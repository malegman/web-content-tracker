package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.StackOverflowUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.StackOverflowQuestionId;

import java.util.Optional;

/**
 * Компонент для получения обновлений из stackoverflow
 */
@FunctionalInterface
public interface FindStackOverflowUpdatesSpi {

    /**
     * Метод для получения обновлений из stackoverflow
     *
     * @param questionId идентификатор вопроса
     *
     * @return {@code empty}, если обновления не найдены
     */
    Optional<StackOverflowUpdatesDto> findStackOverflowUpdates(StackOverflowQuestionId questionId);
}
