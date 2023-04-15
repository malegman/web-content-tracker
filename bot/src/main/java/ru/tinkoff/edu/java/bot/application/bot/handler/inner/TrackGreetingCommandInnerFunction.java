package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

/**
 * Обработчик сообщения команды бота /track, реализует {@link CommandInnerHandler}.<br>
 * Возвращает сообщения для ввода ссылки.
 */
public final class TrackGreetingCommandInnerFunction implements CommandInnerHandler {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getCommand() {
        return "/track";
    }

    @Override
    public Result innerHandle(BotRequest botRequest) {
        return Result.sendMessage(botRequest.tgChatId(), "Введите ссылку для отслеживания.").success();
    }
}
