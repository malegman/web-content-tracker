package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.FindListLinkSpi;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

public final class ListCommandInnerHandler implements CommandInnerHandler {

    private final FindListLinkSpi findListLinkSpi;

    public ListCommandInnerHandler(final FindListLinkSpi findListLinkSpi) {
        this.findListLinkSpi = Objects.requireNonNull(findListLinkSpi);
    }

    @Override
    public Result innerHandle(BotRequest botRequest) {

        final var tgChatId = botRequest.tgChatId();
        return this.findListLinkSpi.findLinks(tgChatId)
                .mapResultOn2xxSuccessful(listLinks ->
                        (listLinks == null || listLinks.size() == 0)
                        ? Result.sendMessage(tgChatId, "Сейчас я ничего не отслеживаю.")
                                .success()
                        : Result.sendMessage(tgChatId, "Ссылки, которые я отслеживаю:")
                                .modifySendMessage(sendMessage -> {
                                    final var inlineKeyboard = new InlineKeyboardMarkup();
                                    listLinks.links().forEach(link -> inlineKeyboard.addRow(
                                            new InlineKeyboardButton(link.link().toString()).url(link.link().toString())));
                                    sendMessage.replyMarkup(inlineKeyboard);})
                                .success())
                .setResultOn4xxClientError(Result.sendMessage(tgChatId, "Сейчас я ничего не отслеживаю.").abort())
                .setResultOnException(Result.sendMessage(tgChatId, "Что-то пошло не так, повторите позже.").abort())
                .getResult();
    }
}
