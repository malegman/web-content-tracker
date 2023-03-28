package ru.tinkoff.edu.java.scrapper.application.webclient;

import java.time.Duration;

/**
 * Компонент для управления расписанием
 *
 * @param interval интервал
 */
public record Scheduler(Duration interval) {
}
