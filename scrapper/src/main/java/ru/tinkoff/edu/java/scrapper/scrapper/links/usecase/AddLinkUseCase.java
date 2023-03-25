package ru.tinkoff.edu.java.scrapper.scrapper.links.usecase;

import ru.tinkoff.edu.java.scrapper.scrapper.links.api.AddLinkApi;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.AddLinkSpi;

import java.util.Objects;

/**
 * Use-case, описывающий логику добавления отслеживаемой ссылки в чат телеграмма
 */
public final class AddLinkUseCase extends AddLinkApi {

    private final AddLinkSpi addLinkSpi;

    public AddLinkUseCase(final AddLinkSpi addLinkSpi) {
        this.addLinkSpi = Objects.requireNonNull(addLinkSpi);
    }

    @Override
    protected Result invokeInternal(Payload payload) {

        try {
            final var url = payload.url();
            final var linkId = this.addLinkSpi.addLink(payload.tgChatId(), url);

            return Result.success(new LinkDto(linkId, payload.url()));
        } catch (Exception e) {
            return Result.executionFailed(e);
        }
    }
}
