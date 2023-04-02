package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

public final class UnTrackGreetingCommandInnerFunction implements CommandInnerHandler {

    @Override
    public Result innerHandle(TgChatId tgChatId, Message message) {
        return Result.success(new SendMessage(tgChatId.value(), "Введите ссылку для отслеживания."));
    }
}
