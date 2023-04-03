package ru.tinkoff.edu.java.bot.common.bot.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExecuteAnotherCommandHandlerFunctionTest {

    ExecuteAnotherCommandHandlerFunction handlerFunction;

    @BeforeEach
    void setUp() {
        this.handlerFunction = new ExecuteAnotherCommandHandlerFunction();
    }

    @Test
    void executeAnotherCommand_Request_ReturnsSuccessResult() {

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
        assertEquals("""
                                Сейчас выполняется другая команда.
                                Отправьте /exit, чтобы прервать текущую команду.""",
                sendMessageParameters.get("text"));
    }
}
