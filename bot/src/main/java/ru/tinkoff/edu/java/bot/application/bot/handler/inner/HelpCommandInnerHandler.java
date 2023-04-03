package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

public final class HelpCommandInnerHandler implements CommandInnerHandler {

    @Override
    public Result innerHandle(BotRequest botRequest) {

        return Result.sendMessage(botRequest.tgChatId(), "Мои команды")
                .modifySendMessage(sendMessage -> sendMessage.replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton("Показать список команд").callbackData("/help"))
                        .addRow(new InlineKeyboardButton("Отслеживать новую ссылку").callbackData("/track"))
                        .addRow(new InlineKeyboardButton("Удалить ссылку из отслеживания").callbackData("/untrack"))
                        .addRow(new InlineKeyboardButton("Показать список отслеживаемых ссылок").callbackData("/list"))))
                .success();
    }
}
