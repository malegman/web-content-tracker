package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TrackGreetingCommandInnerFunctionTest {

    TrackGreetingCommandInnerFunction handler;

    @BeforeEach
    void setUp() {
        this.handler = new TrackGreetingCommandInnerFunction();
    }

    @Test
    void trackGreetingCommand_Request_ReturnsSuccessResult() {

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
        assertEquals("Введите ссылку для отслеживания.",
                sendMessageParameters.get("text"));
    }
}
