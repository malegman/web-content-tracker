package ru.tinkoff.edu.java.scrapper.application.tgchat.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.RegisterTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.RegisterTgChatApi.Result;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.RegisterTgChatApi.Payload;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class RegisterTgChatUseCaseTest {
    
    @Mock
    RegisterTgChatSpi registerTgChatSpi;
    
    RegisterTgChatUseCase useCase;
    
    @BeforeEach
    void setUp() {
        this.useCase = new RegisterTgChatUseCase(this.registerTgChatSpi);
    }

    @Test
    void registerTgChat_PayloadIsValid_ReturnsSuccessResult() {

        // given
        final var id = new TgChatId(1);

        doNothing().when(this.registerTgChatSpi).registerTgChat(id);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(id));

        // then
        assertEquals(Result.success(), result);
        verifyNoMoreInteractions(this.registerTgChatSpi);
    }

    @Test
    void registerTgChat_PayloadIsInvalid_ReturnsValidationFailedResult() {

        // given
        final var tgChatId = TgChatId.valueOf(null);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(tgChatId));

        // then
        assertEquals(Result.validationFailed(new Validation(Map.of(
                Payload.PROP_ID,
                List.of(Payload.ERROR_ID)
        ))), result);
        verifyNoMoreInteractions(this.registerTgChatSpi);
    }

    @Test
    void registerTgChat_ThrowsException_ReturnsExecutionFailedResult() {

        // given
        final var tgChatId = new TgChatId(1);

        final var exception = new RuntimeException();
        doThrow(exception).when(this.registerTgChatSpi).registerTgChat(tgChatId);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(tgChatId));

        // then
        assertEquals(Result.executionFailed(exception), result);
        verifyNoMoreInteractions(this.registerTgChatSpi);
    }
}
