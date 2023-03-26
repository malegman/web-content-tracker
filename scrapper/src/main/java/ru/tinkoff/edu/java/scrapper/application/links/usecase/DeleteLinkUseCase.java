package ru.tinkoff.edu.java.scrapper.application.links.usecase;

import ru.tinkoff.edu.java.scrapper.application.links.api.DeleteLinkApi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.LinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteLinkSpi;

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
            final var url = payload.url();
            final var linkId = this.deleteLinkSpi.deleteLink(payload.tgChatId(), url);

            return Result.success(new LinkDto(linkId, payload.url()));
        } catch (Exception e) {
            return Result.executionFailed(e);
        }
    }
}
