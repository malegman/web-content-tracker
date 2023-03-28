package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.GitHubUpdatesDto;

import java.util.Optional;

/**
 * Компонент для получения обновлений из github
 */
@FunctionalInterface
public interface FindGitHubUpdatesSpi {

    /**
     * Метод для получения обновлений из github
     *
     * @param username имя пользователя
     * @param repo     название репозитория
     *
     * @return {@code empty}, если обновления не найдены
     */
    Optional<GitHubUpdatesDto> findGitHubUpdates(String username, String repo);
}
