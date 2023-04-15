package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.ListLinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.FindListLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandInnerHandler;

import java.util.Objects;

/**
 * Обработчик сообщения команды бота /list, реализует {@link CommandInnerHandler}.<br>
 * Обрабатывает получение списка отслеживаемых ссылок.
 */
public final class ListCommandInnerHandler implements CommandInnerHandler {

    private final FindListLinkSpi findListLinkSpi;

    public ListCommandInnerHandler(final FindListLinkSpi findListLinkSpi) {
        this.findListLinkSpi = Objects.requireNonNull(findListLinkSpi);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String getCommand() {
        return "/list";
    }

    @Override
    public Result innerHandle(BotRequest botRequest) {

        final var tgChatId = botRequest.tgChatId();
        return this.findListLinkSpi.findLinks(tgChatId)
                .mapResultOn2xxSuccessful(listLinks -> this.resultFor2xxSuccessful(tgChatId, listLinks))
                .setResultOn4xxClientError(Result.sendMessage(tgChatId, "Сейчас я ничего не отслеживаю.").abort())
                .setResultOnException(Result.sendMessage(tgChatId, "Что-то пошло не так, повторите позже.").abort())
                .getResult();
    }

    private Result resultFor2xxSuccessful(TgChatId tgChatId, ListLinkScrapperResponse listLinkScrapperResponse) {

        if (listLinkScrapperResponse == null || listLinkScrapperResponse.size() == 0) {
            return Result.sendMessage(tgChatId, "Сейчас я ничего не отслеживаю.").success();
        } else {
            return Result.sendMessage(tgChatId, "Ссылки, которые я отслеживаю:")
                    .modifySendMessage(sendMessage -> {
                        // Создание кнопок для ссылок
                        final var inlineKeyboard = new InlineKeyboardMarkup();
                        listLinkScrapperResponse.links().forEach(link -> inlineKeyboard.addRow(
                                new InlineKeyboardButton(link.link().toString()).url(link.link().toString())));
                        sendMessage.replyMarkup(inlineKeyboard);
                    })
                    .success();
        }
    }
}
