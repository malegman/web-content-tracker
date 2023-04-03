package ru.tinkoff.edu.java.bot.common.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

public final class UnknownCommandHandlerFunction implements HandlerFunction {

    @Override
    public SendMessage handle(BotRequest botRequest) {
        return new SendMessage(botRequest.tgChatId().value(), "Э, говори проще, да /help");
    }
}
