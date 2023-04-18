package ru.tinkoff.edu.java.scrapper.application.links.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.scrapper.application.links.api.FindLinksApi.Payload;
import ru.tinkoff.edu.java.scrapper.application.links.api.FindLinksApi.Result;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindLinksSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkType;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindLinksUseCaseTest {
    
    @Mock
    FindLinksSpi findLinksSpi;
    
    FindLinksUseCase useCase;
    
    @BeforeEach
    void setUp() {
        this.useCase = new FindLinksUseCase(this.findLinksSpi);
    }

    @Test
    void findLinks_PayloadIsValid_ReturnsSuccessResult() {

        // given
        final var tgChatId = new TgChatId(1);

        final var listLinkDto = List.of(new TgChatLinkDto(new TgChatLinkId(1), "url", LinkType.GITHUB));
        doReturn(listLinkDto).when(this.findLinksSpi).findLinks(tgChatId);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId));

        // then
        assertEquals(Result.success(listLinkDto), result);
        verifyNoMoreInteractions(this.findLinksSpi);
    }

    @Test
    void findLinks_PayloadIsInvalid_ReturnsValidationFailedResult() {

        // given
        final var tgChatId = TgChatId.valueOf(null);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId));

        // then
        assertEquals(Result.validationFailed(new Validation(Map.of(
                Payload.PROP_TG_CHAT_ID,
                List.of(Payload.ERROR_TG_CHAT_ID)
        ))), result);
        verifyNoMoreInteractions(this.findLinksSpi);
    }

    @Test
    void findLinks_ThrowsException_ReturnsExecutionFailedResult() {

        // given
        final var tgChatId = new TgChatId(1);

        final var exception = new TgChatNotExistsException();
        doThrow(exception).when(this.findLinksSpi).findLinks(tgChatId);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId));

        // then
        assertEquals(Result.notFound(), result);
        verifyNoMoreInteractions(this.findLinksSpi);
    }
}
