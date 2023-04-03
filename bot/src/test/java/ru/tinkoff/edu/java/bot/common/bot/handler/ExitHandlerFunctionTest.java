package ru.tinkoff.edu.java.bot.common.bot.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExitHandlerFunctionTest {

    ExitHandlerFunction handlerFunction;

    @Test
    void exit_CommandExists_ReturnsSuccessResult() {

        this.handlerFunction = new ExitHandlerFunction(true);

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        // when
        final var sendMessage = handlerFunction.handle(botRequest);

        // then
        final var sendMessageParameters = sendMessage.getParameters();

        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Команда прервана.",
                sendMessageParameters.get("text"));
    }

    @Test
    void exit_CommandNotExists_ReturnsSuccessResult() {

        this.handlerFunction = new ExitHandlerFunction(false);

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        // when
        final var sendMessage = handlerFunction.handle(botRequest);

        // then
        final var sendMessageParameters = sendMessage.getParameters();

        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Я ничего сейчас не выполняю.",
                sendMessageParameters.get("text"));
    }
}
