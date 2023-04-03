package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

/**
 * Обработчик сообщения команды бота /untrack, реализует {@link CommandInnerHandler}.<br>
 * Возвращает сообщения для ввода ссылки.
 */
public final class UnTrackGreetingCommandInnerFunction implements CommandInnerHandler {

    @Override
    public Result innerHandle(BotRequest botRequest) {
        return Result.sendMessage(botRequest.tgChatId(), "Введите ссылку для удаления из отслеживания.").success();
    }
}
