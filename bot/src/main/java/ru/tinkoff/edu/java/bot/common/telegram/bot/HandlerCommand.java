package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

@FunctionalInterface
public interface HandlerCommand {

    SendMessage handle(Update update);
}
