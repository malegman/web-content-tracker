package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

/**
 * Обработчик сообщения команды бота /help, реализует {@link CommandInnerHandler}.<br>
 * Возвращает информацию о боте.
 */
public final class HelpCommandInnerHandler implements CommandInnerHandler {

    @Override
    public Result innerHandle(BotRequest botRequest) {

        return Result.sendMessage(botRequest.tgChatId(), """
                        Я отслеживаю изменения web-ресурсов по ссылкам.  
                        Поддерживаю отслеживание:
                        1. репозиториев github - `https://[{company}.]github.com/{owner}/{repo}`;
                        2. вопросов stackoverflow - `https://stackoverflow.com/questions/{questionId}`.
                          
                        Cписок моих команд:
                        """)
                .modifySendMessage(sendMessage -> sendMessage
                        .replyMarkup(new InlineKeyboardMarkup()
                                .addRow(new InlineKeyboardButton("Показать список команд").callbackData("/help"))
                                .addRow(new InlineKeyboardButton("Отслеживать новую ссылку").callbackData("/track"))
                                .addRow(new InlineKeyboardButton("Удалить ссылку из отслеживания").callbackData("/untrack"))
                                .addRow(new InlineKeyboardButton("Показать список отслеживаемых ссылок").callbackData("/list")))
                        .parseMode(ParseMode.Markdown))
                .success();
    }
}
