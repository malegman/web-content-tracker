package ru.tinkoff.edu.java.scrapper.application.shared.domain.id;

/**
 * Идентификатор репозитория github
 */
public record GitHubRepoId(long value) {

    public static GitHubRepoId valueOf(Long value) {
        return value == null ? null : new GitHubRepoId(value);
    }
}
