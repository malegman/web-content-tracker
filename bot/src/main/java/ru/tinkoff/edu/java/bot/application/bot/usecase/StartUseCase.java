package ru.tinkoff.edu.java.bot.application.bot.usecase;

import ru.tinkoff.edu.java.bot.application.bot.api.StartApi;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.StartSpi;

import java.util.Objects;

public final class StartUseCase extends StartApi {

    private final StartSpi startSpi;

    public StartUseCase(final StartSpi startSpi) {
        this.startSpi = Objects.requireNonNull(startSpi);
    }

    @Override
    protected Result invokeInternal(Payload payload) {
        return Result.success();
    }
}
