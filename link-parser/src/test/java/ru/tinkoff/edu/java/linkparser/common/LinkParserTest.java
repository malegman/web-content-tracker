package ru.tinkoff.edu.java.linkparser.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tinkoff.edu.java.linkparser.github.GitHubLinkParser;
import ru.tinkoff.edu.java.linkparser.github.payload.GitHubPayload;
import ru.tinkoff.edu.java.linkparser.stackoverflow.StackOverflowLinkParser;
import ru.tinkoff.edu.java.linkparser.stackoverflow.domain.id.QuestionId;
import ru.tinkoff.edu.java.linkparser.stackoverflow.payload.StackOverflowPayload;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class LinkParserTest {

    @Mock
    GitHubLinkParser.Result.Processor<String> gitHubPayloadProcessor;

    @Mock
    StackOverflowLinkParser.Result.Processor<String> stackOverflowPayloadProcessor;

    LinkParser<String> parser;

    @BeforeEach
    void setUp() {
        this.parser = LinkParser.link(
                new GitHubLinkParser<>(this.gitHubPayloadProcessor),
                new StackOverflowLinkParser<>(this.stackOverflowPayloadProcessor));
    }

    @Test
    void urlParser_GitHubUrl_ReturnsSuccessResult() {

        final var link = "https://github.com/sanyarnd/tinkoff-java-course-2022/";
        final var user = "sanyarnd";
        final var repo = "tinkoff-java-course-2022";
        final var payload = new GitHubPayload(user, repo);

        final var githubSuccess = "githubSuccess";
        final var success = GitHubLinkParser.Result.success(payload, this.gitHubPayloadProcessor);

        Mockito.doReturn(githubSuccess).when(this.gitHubPayloadProcessor).processSuccess(success);

        final var result = this.parser.parse(link);
        result.visit((value) -> {});

        assertFalse(result.isFailed());
        assertEquals(success, result);
        verifyNoMoreInteractions(this.gitHubPayloadProcessor);
        verifyNoInteractions(this.stackOverflowPayloadProcessor);
    }

    @Test
    void urlParser_StackOverflowUrl_ReturnsSuccessResult() {

        final var link = "https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c";
        final var questionId = new QuestionId(1642028L);
        final var payload = new StackOverflowPayload(questionId);

        final var stackoverflowSuccess = "stackoverflowSuccess";
        final var success = StackOverflowLinkParser.Result.success(payload, this.stackOverflowPayloadProcessor);

        Mockito.doReturn(stackoverflowSuccess).when(this.stackOverflowPayloadProcessor).processSuccess(success);

        final var result = this.parser.parse(link);
        result.visit((value) -> {});

        assertFalse(result.isFailed());
        assertEquals(success, result);
        verifyNoMoreInteractions(this.stackOverflowPayloadProcessor);
        verifyNoInteractions(this.gitHubPayloadProcessor);
    }

    @Test
    void urlParser_UnsupportedUrl_ReturnsEmptyResult() {

        final var link = "https://stackoverflow.com/search?q=unsupported%20link";

        final var result = this.parser.parse(link);

        assertTrue(result.isFailed());
        assertEquals(LinkParser.Result.failed(), result);
        verifyNoInteractions(this.stackOverflowPayloadProcessor, this.gitHubPayloadProcessor);
    }
}
