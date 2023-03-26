package ru.tinkoff.edu.java.scrapper.application.tgchat.usecase;

import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.RegisterTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.RegisterTgChatApi;

import java.util.Objects;

/**
 * Use-case, описывающий логику регистрации чата телеграмма
 */
public final class RegisterTgChatUseCase extends RegisterTgChatApi {

    private final RegisterTgChatSpi registerTgChatSpi;

    public RegisterTgChatUseCase(final RegisterTgChatSpi registerTgChatSpi) {
        this.registerTgChatSpi = Objects.requireNonNull(registerTgChatSpi);
    }

    @Override
    protected Result invokeInternal(Payload payload) {

        try {
            this.registerTgChatSpi.registerTgChat(payload.id());
            return Result.success();
        } catch (Exception e) {
            return Result.executionFailed(e);
        }
    }
}
