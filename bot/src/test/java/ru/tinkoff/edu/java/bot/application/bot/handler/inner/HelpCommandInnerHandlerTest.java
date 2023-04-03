package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class HelpCommandInnerHandlerTest {

    HelpCommandInnerHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new HelpCommandInnerHandler();
    }

    @Test
    void helpCommand_Request_ReturnsSuccessResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isSuccess());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("""
                        Я отслеживаю изменения web-ресурсов по ссылкам.  
                        Поддерживаю отслеживание:
                        1. репозиториев github - `https://[{company}.]github.com/{owner}/{repo}`;
                        2. вопросов stackoverflow - `https://stackoverflow.com/questions/{questionId}`.
                          
                        Cписок моих команд:
                        """,
                sendMessageParameters.get("text"));
        assertEquals(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton("Показать список команд").callbackData("/help"))
                        .addRow(new InlineKeyboardButton("Отслеживать новую ссылку").callbackData("/track"))
                        .addRow(new InlineKeyboardButton("Удалить ссылку из отслеживания").callbackData("/untrack"))
                        .addRow(new InlineKeyboardButton("Показать список отслеживаемых ссылок").callbackData("/list")),
                sendMessageParameters.get("reply_markup"));
        assertEquals(ParseMode.Markdown,
                ParseMode.valueOf((String) sendMessageParameters.get("parse_mode")));
    }
}
