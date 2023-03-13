package ru.tinkoff.edu.java.linkparser.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.linkparser.github.GitHubUrlParser;
import ru.tinkoff.edu.java.linkparser.github.payload.GitHubPayload;
import ru.tinkoff.edu.java.linkparser.stackoverflow.StackOverflowUrlParser;
import ru.tinkoff.edu.java.linkparser.stackoverflow.payload.StackOverflowPayload;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UrlParserTest {

    @Mock
    GitHubUrlParser.Result.Processor<String> gitHubPayloadProcessor;

    @Mock
    StackOverflowUrlParser.Result.Processor<String> stackOverflowPayloadProcessor;

    UrlParser<String> parser;

    @BeforeEach
    void setUp() {
        this.parser = UrlParser.link(
                new GitHubUrlParser<>(this.gitHubPayloadProcessor),
                new StackOverflowUrlParser<>(this.stackOverflowPayloadProcessor));
    }

    @Test
    void parserChain_GitHubUrl_ReturnsSuccessResult() {

        final var link = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final var user = "sanyarnd";
        final var repo = "tinkoff-java-course-2022";
        final var payload = new GitHubPayload(user, repo);

        final var githubSuccess = "githubSuccess";
        final var success = GitHubUrlParser.Result.success(payload, this.gitHubPayloadProcessor);

        Mockito.doReturn(githubSuccess).when(this.gitHubPayloadProcessor).processSuccess(success);

        final var result = this.parser.parse(link);

        assertFalse(result.isFailed());
        assertEquals(success, result);
        assertEquals(githubSuccess, result.process());
        verifyNoMoreInteractions(this.gitHubPayloadProcessor);
        verifyNoInteractions(this.stackOverflowPayloadProcessor);
    }

    @Test
    void parserChain_StackOverflowUrl_ReturnsSuccessResult() {

        final var link = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        final var questionId = 1642028L;
        final var payload = new StackOverflowPayload(questionId);

        final var stackoverflowSuccess = "stackoverflowSuccess";
        final var success = StackOverflowUrlParser.Result.success(payload, this.stackOverflowPayloadProcessor);

        Mockito.doReturn(stackoverflowSuccess).when(this.stackOverflowPayloadProcessor).processSuccess(success);

        final var result = this.parser.parse(link);

        assertFalse(result.isFailed());
        assertEquals(success, result);
        assertEquals(stackoverflowSuccess, result.process());
        verifyNoMoreInteractions(this.stackOverflowPayloadProcessor);
        verifyNoInteractions(this.gitHubPayloadProcessor);
    }

    @Test
    void parserChain_UnsupportedUrl_ReturnsEmptyResult() {

        final var link = "https://stackoverflow.com/search?q=unsupported%20link";

        final var result = this.parser.parse(link);

        assertTrue(result.isFailed());
        assertEquals(UrlParser.Result.failed(), result);
        assertNull(result.process());
        verifyNoInteractions(this.stackOverflowPayloadProcessor, this.gitHubPayloadProcessor);
    }
}
