package ru.tinkoff.edu.java.scrapper.application.links.usecase;

import ru.tinkoff.edu.java.scrapper.application.links.api.DeleteLinkApi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteLinkSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;

import java.util.Objects;

/**
 * Use-case, описывающий логику удаления отслеживаемой ссылки чата телеграмма
 */
public final class DeleteLinkUseCase extends DeleteLinkApi {

    private final DeleteLinkSpi deleteLinkSpi;

    public DeleteLinkUseCase(final DeleteLinkSpi deleteLinkSpi) {
        this.deleteLinkSpi = Objects.requireNonNull(deleteLinkSpi);
    }

    @Override
    protected Result invokeInternal(Payload payload) {

        try {
            final var url = payload.link();
            final var linkId = this.deleteLinkSpi.deleteLink(payload.tgChatId(), url);

            return Result.success(new TgChatLinkDto(linkId, payload.link()));
        } catch (TgChatLinkNotExistsException | TgChatNotExistsException e) {
            return Result.notFound();
        }
    }
}
