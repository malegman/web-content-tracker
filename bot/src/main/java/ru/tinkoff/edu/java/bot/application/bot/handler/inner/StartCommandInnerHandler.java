package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddChatSpi;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

/**
 * Обработчик сообщения команды бота /start, реализует {@link CommandInnerHandler}.<br>
 * Обрабатывает добавление чата.
 */
public final class StartCommandInnerHandler implements CommandInnerHandler {

    private final AddChatSpi addChatSpi;

    public StartCommandInnerHandler(final AddChatSpi addChatSpi) {
        this.addChatSpi = Objects.requireNonNull(addChatSpi);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public Result innerHandle(BotRequest botRequest) {

        final var tgChatId = botRequest.tgChatId();
        return this.addChatSpi.addChat(tgChatId)
                .setResultOn2xxSuccessful(Result
                        .sendMessage(tgChatId, "Привет! Я бот для отслеживания web-ресурсов. /help")
                        .modifySendMessage(sendMessage -> sendMessage.replyMarkup(
                                new ReplyKeyboardMarkup(
                                        new String[][]{{"/help", "/list"}, {"/track", "/untrack"}})
                                .resizeKeyboard(true)))
                        .success())
                .setResultOn4xxClientError(Result.sendMessage(tgChatId, "Я уже Вас знаю!").abort())
                .setResultOnException(Result.sendMessage(tgChatId, "Что-то пошло не так, повторите позже.").abort())
                .getResult();
    }
}