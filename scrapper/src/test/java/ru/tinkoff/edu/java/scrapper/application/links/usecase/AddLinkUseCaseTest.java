package ru.tinkoff.edu.java.scrapper.application.links.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.scrapper.application.links.api.AddLinkApi.Payload;
import ru.tinkoff.edu.java.scrapper.application.links.api.AddLinkApi.Result;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkGitHubSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkStackOverflowSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindGitHubUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindStackOverflowUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkType;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddLinkUseCaseTest {

    @Mock
    AddLinkGitHubSpi addLinkGitHubSpi;
    @Mock
    AddLinkStackOverflowSpi addLinkStackOverflowSpi;
    @Mock
    FindGitHubUpdatesSpi findGitHubUpdatesSpi;
    @Mock
    FindStackOverflowUpdatesSpi findStackOverflowUpdatesSpi;

    AddLinkUseCase useCase;

    @BeforeEach
    void setUp() {
        this.useCase = new AddLinkUseCase(this.addLinkGitHubSpi, this.addLinkStackOverflowSpi,
                this.findGitHubUpdatesSpi, this.findStackOverflowUpdatesSpi);
    }

    @Test
    void addLink_PayloadIsValid_ReturnsSuccessResult() {

        // given
        final var url = "url";
        final var tgChatId = new TgChatId(1);

        final var tgChatLinkId = new TgChatLinkId(1);
        doReturn(tgChatLinkId).when(this.addLinkGitHubSpi).addLinkGitHub(tgChatId, url, null); // временная заглушка

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId)
                .link(url));

        // then
        assertEquals(Result.success(new TgChatLinkDto(tgChatLinkId, url, LinkType.GITHUB)), result);
        verifyNoMoreInteractions(this.addLinkGitHubSpi);
    }

    @Test
    void addLink_PayloadIsInvalid_ReturnsValidationFailedResult() {

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
        verifyNoMoreInteractions(this.addLinkGitHubSpi);
    }

    @Test
    void addLink_ThrowsException_ReturnsExecutionFailedResult() {

        // given
        final var url = "url";
        final var tgChatId = new TgChatId(1);

        final var exception = new RuntimeException();
        doThrow(exception).when(this.addLinkGitHubSpi).addLinkGitHub(tgChatId, url, null); // временная заглушка

        // when
        final var result = this.useCase.invoke(builder -> builder
                .tgChatId(tgChatId)
                .link(url));

        // then
        assertEquals(Result.linkAlreadyExists(), result);
        verifyNoMoreInteractions(this.addLinkGitHubSpi);
    }
}
