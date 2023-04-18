package ru.tinkoff.edu.java.scrapper.application.tgchat.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.DeleteTgChatApi.Payload;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.DeleteTgChatApi.Result;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteTgChatUseCaseTest {
    
    @Mock
    DeleteTgChatSpi deleteTgChatSpi;
    
    DeleteTgChatUseCase useCase;
    
    @BeforeEach
    void setUp() {
        this.useCase = new DeleteTgChatUseCase(this.deleteTgChatSpi);
    }

    @Test
    void deleteTgChat_PayloadIsValid_ReturnsSuccessResult() {

        // given
        final var id = new TgChatId(1);
        
        doNothing().when(this.deleteTgChatSpi).deleteTgChat(id);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(id));

        // then
        assertEquals(Result.success(), result);
        verifyNoMoreInteractions(this.deleteTgChatSpi);
    }

    @Test
    void deleteTgChat_PayloadIsInvalid_ReturnsValidationFailedResult() {

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
        verifyNoMoreInteractions(this.deleteTgChatSpi);
    }

    @Test
    void deleteTgChat_ThrowsException_ReturnsExecutionFailedResult() {

        // given
        final var tgChatId = new TgChatId(1);

        final var exception = new RuntimeException();
        doThrow(exception).when(this.deleteTgChatSpi).deleteTgChat(tgChatId);

        // when
        final var result = this.useCase.invoke(builder -> builder
                .id(tgChatId));

        // then
        assertEquals(Result.notFound(), result);
        verifyNoMoreInteractions(this.deleteTgChatSpi);
    }
}
