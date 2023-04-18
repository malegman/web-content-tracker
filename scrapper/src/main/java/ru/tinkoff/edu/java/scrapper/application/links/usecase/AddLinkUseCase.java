package ru.tinkoff.edu.java.scrapper.application.links.usecase;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.linkparser.common.LinkParser;
import ru.tinkoff.edu.java.linkparser.github.GitHubLinkParser;
import ru.tinkoff.edu.java.linkparser.stackoverflow.StackOverflowLinkParser;
import ru.tinkoff.edu.java.scrapper.application.links.api.AddLinkApi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkGitHubSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkStackOverflowSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindGitHubUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindStackOverflowUpdatesSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.StackOverflowQuestionId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.common.validation.Validation;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Use-case, описывающий логику добавления отслеживаемой ссылки в чат телеграмма
 */
public final class AddLinkUseCase extends AddLinkApi {

    private final AddLinkGitHubSpi addLinkGitHubSpi;
    private final AddLinkStackOverflowSpi addLinkStackOverflowSpi;
    private final FindGitHubUpdatesSpi findGitHubUpdatesSpi;
    private final FindStackOverflowUpdatesSpi findStackOverflowUpdatesSpi;

    public AddLinkUseCase(final AddLinkGitHubSpi addLinkGitHubSpi,
                          final AddLinkStackOverflowSpi addLinkStackOverflowSpi,
                          final FindGitHubUpdatesSpi findGitHubUpdatesSpi,
                          final FindStackOverflowUpdatesSpi findStackOverflowUpdatesSpi) {
        this.addLinkGitHubSpi = Objects.requireNonNull(addLinkGitHubSpi);
        this.addLinkStackOverflowSpi = Objects.requireNonNull(addLinkStackOverflowSpi);
        this.findGitHubUpdatesSpi = Objects.requireNonNull(findGitHubUpdatesSpi);
        this.findStackOverflowUpdatesSpi = Objects.requireNonNull(findStackOverflowUpdatesSpi);
    }

    @Override
    protected Result invokeInternal(Payload payload) {

        final var visitor = new LinkParserVisitor(payload.tgChatId(), payload.link());
        final var linkParser = LinkParser.link(
                new GitHubLinkParser<>(visitor.gitHubLinkProcessor()),
                new StackOverflowLinkParser<>(visitor.stackOverflowLinkProcessor()));

        final var parseResult = linkParser.parse(payload.link());
        if (parseResult.isFailed()) {
            return Result.validationFailed(new Validation(Map.of(
                    Payload.PROP_LINK, List.of(Payload.ERROR_LINK))));
        }
        parseResult.visit(visitor);

        return visitor.result;
    }

    private final class LinkParserVisitor implements
            LinkParser.Result.Visitor<Result> {

        private final TgChatId tgChatId;
        private final String link;
        private Result result;

        private LinkParserVisitor(final TgChatId tgChatId,
                                  final String link) {
            this.tgChatId = Objects.requireNonNull(tgChatId);
            this.link = Objects.requireNonNull(link);
        }

        private GitHubLinkProcessor gitHubLinkProcessor() {
            return new GitHubLinkProcessor();
        }

        private StackOverflowLinkProcessor stackOverflowLinkProcessor() {
            return new StackOverflowLinkProcessor();
        }

        @Override
        public void setValue(Result value) {
            this.result = value;
        }

        private final class GitHubLinkProcessor implements GitHubLinkParser.Result.Processor<Result> {

            @Override
            public Result processSuccess(GitHubLinkParser.Result.Success<Result> result) {
                final var payload = result.payload();
                final var dtoOpt = findGitHubUpdatesSpi.findGitHubUpdates(payload.user(), payload.repo());

                if (dtoOpt.isEmpty()) {
                    return Result.validationFailed(new Validation(Map.of(
                            Payload.PROP_LINK, List.of(Payload.ERROR_LINK))));
                }

                try {
                    final var tgChatLinkId = addLinkGitHubSpi.addLinkGitHub(tgChatId, link, dtoOpt.get());
                    return Result.success(new TgChatLinkDto(tgChatLinkId, link));
                } catch (TgChatNotExistsException e) {
                    return Result.validationFailed(new Validation(Map.of(
                            Payload.PROP_TG_CHAT_ID, List.of(Payload.ERROR_TG_CHAT_ID))));
                } catch (TgChatLinkAlreadyExistsException e) {
                    return Result.linkAlreadyExists();
                }
            }
        }

        private final class StackOverflowLinkProcessor implements StackOverflowLinkParser.Result.Processor<Result> {

            @Override
            public Result processSuccess(StackOverflowLinkParser.Result.Success<Result> result) {
                final var payload = result.payload();
                final var dtoOpt = findStackOverflowUpdatesSpi.findStackOverflowUpdates(
                        new StackOverflowQuestionId(payload.questionId().value()));

                if (dtoOpt.isEmpty()) {
                    return Result.validationFailed(new Validation(Map.of(
                            Payload.PROP_LINK, List.of(Payload.ERROR_LINK))));
                }

                try {
                    final var tgChatLinkId = addLinkStackOverflowSpi.addLinkStackOverflow(tgChatId, link, dtoOpt.get());
                    return Result.success(new TgChatLinkDto(tgChatLinkId, link));
                } catch (TgChatNotExistsException e) {
                    return Result.validationFailed(new Validation(Map.of(
                            Payload.PROP_TG_CHAT_ID, List.of(Payload.ERROR_TG_CHAT_ID))));
                } catch (TgChatLinkAlreadyExistsException e) {
                    return Result.linkAlreadyExists();
                }
            }
        }
    }
}
