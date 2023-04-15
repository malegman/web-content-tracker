package ru.tinkoff.edu.java.bot.application.client.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi.Payload;
import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi.Result;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.SendUpdatesSpi;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.api.validation.Validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SendUpdatesUseCaseTest {

    @Mock
    SendUpdatesSpi sendUpdatesSpi;

    SendUpdatesUseCase useCase;

    @BeforeEach
    void setUp() {
        this.useCase = new SendUpdatesUseCase(this.sendUpdatesSpi);
    }

    @Test
    void sendUpdates_PayloadIsValid_ReturnsSuccessResult() {

        // given
        final var id = new LinkId(1);
        final var url = "url";
        final var description = "description";
        final var tgChatIds = List.of(new TgChatId(1), new TgChatId(2));

        doNothing().when(this.sendUpdatesSpi).sendUpdates(id, url, description, tgChatIds);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(id)
                .url(url)
                .description(description)
                .tgChatIds(tgChatIds));

        // then
        assertEquals(Result.success(), result);
        verifyNoMoreInteractions(this.sendUpdatesSpi);
    }

    @Test
    void sendUpdates_PayloadIsInvalid_ReturnsValidationFailedResult() {

        // given
        final var id = LinkId.valueOf(null);
        final var url = "  ";
        final var description = "";
        final var tgChatIds = new ArrayList<>(List.of(new TgChatId(1)));
        tgChatIds.add(null);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(id)
                .url(url)
                .description(description)
                .tgChatIds(tgChatIds));

        // then
        assertEquals(Result.validationFailed(new Validation(Map.of(
                Payload.PROP_ID,
                List.of(Payload.ERROR_ID),
                Payload.PROP_URL,
                List.of(Payload.ERROR_URL),
                Payload.PROP_DESCRIPTION,
                List.of(Payload.ERROR_DESCRIPTION),
                Payload.PROP_TG_CHAT_IDS,
                List.of(Payload.ERROR_TG_CHAT_IDS)
        ))), result);
        verifyNoInteractions(this.sendUpdatesSpi);
    }

    @Test
    void sendUpdates_EmptyTgChatIds_ReturnsValidationFailedResult() {

        // given
        final var id = new LinkId(1);
        final var url = "url";
        final var description = "description";
        final var tgChatIds = List.<TgChatId>of();

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(id)
                .url(url)
                .description(description)
                .tgChatIds(tgChatIds));

        // then
        assertEquals(Result.validationFailed(new Validation(Map.of(
                Payload.PROP_TG_CHAT_IDS,
                List.of(Payload.ERROR_TG_CHAT_IDS)
        ))), result);
        verifyNoInteractions(this.sendUpdatesSpi);
    }

    @Test
    void sendUpdates_ThrowsException_ReturnsExecutionFailedResult() {

        // given
        final var id = new LinkId(1);
        final var url = "url";
        final var description = "description";
        final var tgChatIds = List.of(new TgChatId(1), new TgChatId(2));

        final var exception = new RuntimeException();
        doThrow(exception).when(this.sendUpdatesSpi).sendUpdates(id, url, description, tgChatIds);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(id)
                .url(url)
                .description(description)
                .tgChatIds(tgChatIds));

        // then
        assertEquals(Result.executionFailed(exception), result);
        verifyNoMoreInteractions(this.sendUpdatesSpi);
    }
}
