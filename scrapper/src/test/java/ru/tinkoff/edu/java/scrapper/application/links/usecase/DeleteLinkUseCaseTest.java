package ru.tinkoff.edu.java.scrapper.application.links.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.scrapper.application.links.api.DeleteLinkApi.Payload;
import ru.tinkoff.edu.java.scrapper.application.links.api.DeleteLinkApi.Result;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteLinkSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class DeleteLinkUseCaseTest {
    
    @Mock
    DeleteLinkSpi deleteLinkSpi;
    
    DeleteLinkUseCase useCase;
    
    @BeforeEach
    void setUp() {
        this.useCase = new DeleteLinkUseCase(this.deleteLinkSpi);
    }

    @Test
    void deleteLink_PayloadIsValid_ReturnsSuccessResult() {

        // given
        final var url = "url";
        final var tgChatId = new TgChatId(1);

        final var tgChatLinkId = new TgChatLinkId(1);
        doReturn(tgChatLinkId).when(this.deleteLinkSpi).deleteLink(tgChatId, url);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId)
                .link(url));

        // then
        assertEquals(Result.success(new TgChatLinkDto(tgChatLinkId, url, null)), result);
        verifyNoMoreInteractions(this.deleteLinkSpi);
    }

    @Test
    void deleteLink_PayloadIsInvalid_ReturnsValidationFailedResult() {

        // given
        final var url = " ";
        final var tgChatId = TgChatId.valueOf(null);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId)
                .link(url));

        // then
        assertEquals(Result.validationFailed(new Validation(Map.of(
                Payload.PROP_TG_CHAT_ID,
                List.of(Payload.ERROR_TG_CHAT_ID),
                Payload.PROP_LINK,
                List.of(Payload.ERROR_LINK)
        ))), result);
        verifyNoMoreInteractions(this.deleteLinkSpi);
    }

    @Test
    void deleteLink_ThrowsException_ReturnsExecutionFailedResult() {

        // given
        final var url = "url";
        final var tgChatId = new TgChatId(1);

        final var exception = new RuntimeException();
        doThrow(exception).when(this.deleteLinkSpi).deleteLink(tgChatId, url);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId)
                .link(url));

        // then
        assertEquals(Result.notFound(), result);
        verifyNoMoreInteractions(this.deleteLinkSpi);
    }
}
