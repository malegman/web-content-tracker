package ru.tinkoff.edu.java.linkparser.common;

public class LinkParserChainFactory {

    public static AbstractLinkParser linkParsersChainOf(final AbstractLinkParser... parsers) {

        if (parsers == null || parsers.length == 0) {
            return new DefaultLinkParser();
        }

        final var parserMaxIndex = parsers.length - 1;
        for (int parserIndex = 0; parserIndex < parserMaxIndex; parserIndex++) {
            parsers[parserIndex].setNext(parsers[parserIndex + 1]);
        }

        return parsers[0];
    }

    private static final class DefaultLinkParser extends AbstractLinkParser {

        @Override
        protected Result doInternalParse(String link) {
            return Result.failed();
        }
    }
}
