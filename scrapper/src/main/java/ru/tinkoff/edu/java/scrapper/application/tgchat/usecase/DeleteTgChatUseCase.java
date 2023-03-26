package ru.tinkoff.edu.java.scrapper.application.tgchat.usecase;

import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.DeleteTgChatApi;

import java.util.Objects;

/**
 * Use-case, описывающий логику удаления чата телеграмма
 */
public final class DeleteTgChatUseCase extends DeleteTgChatApi {
    
    private final DeleteTgChatSpi deleteTgChatSpi;
    
    public DeleteTgChatUseCase(final DeleteTgChatSpi deleteTgChatSpi) {
        this.deleteTgChatSpi = Objects.requireNonNull(deleteTgChatSpi);
    }
    
    @Override
    protected Result invokeInternal(Payload payload) {

        try {
            this.deleteTgChatSpi.deleteTgChat(payload.id());
            return Result.success();
        } catch (Exception e) {
            return Result.executionFailed(e);
        }
    }
}
