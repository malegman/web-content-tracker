package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddChatSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class StartCommandInnerHandlerTest {

    @Mock
    AddChatSpi addChatSpi;

    StartCommandInnerHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new StartCommandInnerHandler(this.addChatSpi);
    }

    @Test
    void startCommand_Response2xx_ReturnsSuccessResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        doReturn(new WebClientBodyResponse<>(HttpStatus.NO_CONTENT, null))
                .when(this.addChatSpi).addChat(tgChatId);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.addChatSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isSuccess());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Привет! Я бот для отслеживания web-ресурсов. /help",
                sendMessageParameters.get("text"));
    }

    @Test
    void startCommand_Response4xx_ReturnsAbortResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var e = new WebClientResponseException(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        doReturn(WebClientBodyResponse.fromWebClientResponseException(e)).when(this.addChatSpi).addChat(tgChatId);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.addChatSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isAbort());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Я уже Вас знаю!",
                sendMessageParameters.get("text"));
    }

    @Test
    void startCommand_ResponseException_ReturnsAbortResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var e = new Exception();
        doReturn(WebClientBodyResponse.fromException(e)).when(this.addChatSpi).addChat(tgChatId);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.addChatSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isAbort());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Что-то пошло не так, повторите позже.",
                sendMessageParameters.get("text"));
    }
}
