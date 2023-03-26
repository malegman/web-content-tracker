package ru.tinkoff.edu.java.bot.application.client.usecase;

import ru.tinkoff.edu.java.bot.application.client.api.SendUpdatesApi;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.SendUpdatesSpi;

import java.util.Objects;

/**
 * Use-case, описывающий логику рассылки обновления ссылки в указанные телеграмм чаты
 */
public final class SendUpdatesUseCase extends SendUpdatesApi {

    private final SendUpdatesSpi sendUpdatesSpi;

    public SendUpdatesUseCase(final SendUpdatesSpi sendUpdatesSpi) {
        this.sendUpdatesSpi = Objects.requireNonNull(sendUpdatesSpi);
    }

    @Override
    protected Result invokeInternal(Payload payload) {

        try {
            this.sendUpdatesSpi.sendUpdates(payload.id(), payload.url(),
                    payload.description(), payload.tgChatIds());
            return Result.success();
        } catch (Exception e) {
            return Result.executionFailed(e);
        }
    }
}
