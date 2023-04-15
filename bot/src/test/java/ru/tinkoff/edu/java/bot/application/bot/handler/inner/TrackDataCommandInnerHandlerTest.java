package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.LinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.AddLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class TrackDataCommandInnerHandlerTest {
    
    @Mock
    AddLinkSpi addLinkSpi;

    TrackDataCommandInnerHandler handler;
    
    @BeforeEach
    void setUp() {
        this.handler = new TrackDataCommandInnerHandler(this.addLinkSpi);
    }

    @Test
    void trackDataCommand_Response2xx_ReturnsSuccessResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var linkResponse = new LinkScrapperResponse(1L, URI.create(link));
        doReturn(new WebClientBodyResponse<>(HttpStatus.OK, linkResponse))
                .when(this.addLinkSpi).addLink(tgChatId, link);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.addLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isSuccess());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Ссылка успешно добавлена!",
                sendMessageParameters.get("text"));
    }

    @Test
    void trackDataCommand_Response4xx_ReturnsRepeatResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var e = new WebClientResponseException(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        doReturn(WebClientBodyResponse.fromWebClientResponseException(e)).when(this.addLinkSpi).addLink(tgChatId, link);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.addLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isRepeat());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("""
                                Проверьте корректность ссылки и введите ссылку ещё раз.
                                Отправьте /exit, чтобы прервать команду.""",
                sendMessageParameters.get("text"));
    }

    @Test
    void trackDataCommand_Response409_ReturnsAbortResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var e = new WebClientResponseException(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(), null, null, null);
        doReturn(WebClientBodyResponse.fromWebClientResponseException(e)).when(this.addLinkSpi).addLink(tgChatId, link);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.addLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isAbort());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Эта ссылка уже отслеживается.",
                sendMessageParameters.get("text"));
    }

    @Test
    void trackDataCommand_ResponseException_ReturnsAbortResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var e = new Exception();
        doReturn(WebClientBodyResponse.fromException(e)).when(this.addLinkSpi).addLink(tgChatId, link);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.addLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isAbort());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Что-то пошло не так, повторите позже.",
                sendMessageParameters.get("text"));
    }
}
