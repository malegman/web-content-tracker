package ru.tinkoff.edu.java.bot.application.bot.handler.inner;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.LinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.ListLinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.FindListLinkSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ListCommandInnerHandlerTest {

    @Mock
    FindListLinkSpi findListLinkSpi;

    ListCommandInnerHandler handler;

    @BeforeEach
    void setUp() {
        this.handler = new ListCommandInnerHandler(this.findListLinkSpi);
    }

    @Test
    void listCommand_FindList2xx_ReturnsSuccessResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var responseBody = new ListLinkScrapperResponse(
                List.of(new LinkScrapperResponse(1L, URI.create(link))),
                1);
        doReturn(new WebClientBodyResponse<>(HttpStatus.OK, responseBody))
                .when(this.findListLinkSpi).findLinks(tgChatId);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.findListLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isSuccess());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Ссылки, которые я отслеживаю:",
                sendMessageParameters.get("text"));
        assertEquals(new InlineKeyboardMarkup()
                        .addRow(new InlineKeyboardButton(link).url(link)),
                sendMessageParameters.get("reply_markup"));
    }

    @Test
    void listCommand_FindEmptyList2xx_ReturnsSuccessResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var responseBody = new ListLinkScrapperResponse(List.of(), 0);
        doReturn(new WebClientBodyResponse<>(HttpStatus.OK, responseBody))
                .when(this.findListLinkSpi).findLinks(tgChatId);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.findListLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isSuccess());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Сейчас я ничего не отслеживаю.",
                sendMessageParameters.get("text"));
    }

    @Test
    void listCommand_Response4xx_ReturnsAbortResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var e = new WebClientResponseException(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        doReturn(WebClientBodyResponse.fromWebClientResponseException(e)).when(this.findListLinkSpi).findLinks(tgChatId);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.findListLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isAbort());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Сейчас я ничего не отслеживаю.",
                sendMessageParameters.get("text"));
    }

    @Test
    void listCommand_ResponseException_ReturnsAbortResult() {

        // given
        final var tgChatId = new TgChatId(1);
        final var link = "url";
        final var botRequest = new BotRequest(tgChatId, link);

        final var e = new Exception();
        doReturn(WebClientBodyResponse.fromException(e)).when(this.findListLinkSpi).findLinks(tgChatId);

        // when
        final var result = handler.innerHandle(botRequest);

        // then
        verifyNoMoreInteractions(this.findListLinkSpi);

        final var sendMessageParameters = result.sendMessage().getParameters();

        assertTrue(result.isAbort());
        assertEquals(tgChatId.value(),
                sendMessageParameters.get("chat_id"));
        assertEquals("Что-то пошло не так, повторите позже.",
                sendMessageParameters.get("text"));
    }
}
