package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

public final class UnTrackGreetingCommandInnerFunction implements CommandInnerHandler {

    @Override
    public Result innerHandle(BotRequest botRequest) {
        return Result.sendMessage(botRequest.tgChatId(), "Введите ссылку для отслеживания.").success();
    }
}
