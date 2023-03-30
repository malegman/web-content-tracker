package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Update;

@FunctionalInterface
public interface Handler {

    void handle(Update update);
}
