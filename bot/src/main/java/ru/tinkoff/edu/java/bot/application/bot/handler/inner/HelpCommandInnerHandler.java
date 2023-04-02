package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

public final class HelpCommandInnerHandler implements CommandInnerHandler {

    @Override
    public Result innerHandle(TgChatId tgChatId, Message message) {

        return Result.sendMessage(tgChatId, "Я могу много чего!")
                .modifySendMessage(sendMessage -> sendMessage.replyMarkup(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton("Покажи команды").callbackData("/help"))
                        .addRow(new InlineKeyboardButton("Добавь новую ссылку").callbackData("/track"))
                        .addRow(new InlineKeyboardButton("Удали ссылку").callbackData("/untrack"))
                        .addRow(new InlineKeyboardButton("Покажи мои ссылки").callbackData("/list"))))
                .success();
    }
}
