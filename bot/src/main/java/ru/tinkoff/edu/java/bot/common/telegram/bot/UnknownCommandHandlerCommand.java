package ru.tinkoff.edu.java.bot.common.telegram.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class UnknownCommandHandlerCommand implements HandlerCommand {

    @Override
    public SendMessage handle(final Update update) {
        return new SendMessage(update.message().chat().id(), "Unknown command");
    }
}
