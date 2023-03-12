package ru.tinkoff.edu.java.linkparser.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.linkparser.common.LinkParser.Result;
import ru.tinkoff.edu.java.linkparser.common.LinkParser.Result.Processor;
import ru.tinkoff.edu.java.linkparser.github.GitHubChainLinkParser;
import ru.tinkoff.edu.java.linkparser.github.payload.GitHubPayload;
import ru.tinkoff.edu.java.linkparser.stackoverflow.payload.StackOverflowPayload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class ParserChainTest {

    @Mock
    Processor<Object, GitHubPayload> gitHubPayloadProcessor;

    @Mock
    Processor<Object, StackOverflowPayload> stackOverflowPayloadProcessor;

    @Test
    void parserChain_OnlyGitHub_ReturnsSuccessResult() {

        final var parser = ChainLinkParserFactory.chainOf(
                new GitHubChainLinkParser<>(this.gitHubPayloadProcessor));

        final var link = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final var user = "sanyarnd";
        final var repo = "tinkoff-java-course-2022";
        final var payload = new GitHubPayload(user, repo);

        final Object objectSuccess = 1;
        final var success = Result.success(payload, this.gitHubPayloadProcessor);

        Mockito.doReturn(objectSuccess).when(this.gitHubPayloadProcessor).processSuccess(success);

        final var result = parser.doParse(link);

        assertEquals(success, result);
        assertEquals(objectSuccess, result.process());
        verifyNoMoreInteractions(this.gitHubPayloadProcessor);
        verifyNoInteractions(this.stackOverflowPayloadProcessor);
    }
}
