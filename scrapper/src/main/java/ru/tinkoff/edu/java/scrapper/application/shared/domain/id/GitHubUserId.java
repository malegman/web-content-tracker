package ru.tinkoff.edu.java.scrapper.application.shared.domain.id;

/**
 * Идентификатор пользователя github
 */
public record GitHubUserId(Long value) {

    public static GitHubUserId valueOf(Long value) {
        return value == null ? null : new GitHubUserId(value);
    }
}
