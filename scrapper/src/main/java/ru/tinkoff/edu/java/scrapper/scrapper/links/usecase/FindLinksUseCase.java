package ru.tinkoff.edu.java.scrapper.scrapper.links.usecase;

import ru.tinkoff.edu.java.scrapper.scrapper.links.api.FindLinksApi;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.FindLinksSpi;

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
            return Result.success(this.findLinksSpi.findLinks(payload.tgChatId()));
        } catch (Exception e) {
            return Result.executionFailed(e);
        }
    }
}
