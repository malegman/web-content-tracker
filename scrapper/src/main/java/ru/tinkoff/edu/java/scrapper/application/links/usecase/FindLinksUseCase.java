package ru.tinkoff.edu.java.scrapper.application.links.usecase;

import ru.tinkoff.edu.java.scrapper.application.links.api.FindLinksApi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindLinksSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;

import java.util.Objects;

/**
 * Use-case, описывающий логику получения отслеживаемых ссылок чата телеграмма
 */
public final class FindLinksUseCase extends FindLinksApi {

    private final FindLinksSpi findLinksSpi;

    public FindLinksUseCase(final FindLinksSpi findLinksSpi) {
        this.findLinksSpi = Objects.requireNonNull(findLinksSpi);
    }

    @Override
    protected Result invokeInternal(Payload payload) {

        try {
            final var links = this.findLinksSpi.findLinks(payload.tgChatId());
            if (links.isEmpty()) {
                return Result.notFound();
            } else {
                return Result.success(links);
            }
        } catch (TgChatNotExistsException e) {
            return Result.notFound();
        }
    }
}
