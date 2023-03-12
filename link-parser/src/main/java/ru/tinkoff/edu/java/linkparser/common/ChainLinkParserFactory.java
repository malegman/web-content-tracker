package ru.tinkoff.edu.java.linkparser.common;

public class ChainLinkParserFactory {

    public static LinkParser chainOf(final AbstractChainLinkParser... parsers) {

        if (parsers == null || parsers.length == 0) {
            return new DefaultChainLinkParser();
        }

        final var parserMaxIndex = parsers.length - 1;
        for (int parserIndex = 0; parserIndex < parserMaxIndex; parserIndex++) {
            parsers[parserIndex].setNext(parsers[parserIndex + 1]);
        }

        return parsers[0];
    }

    private static final class DefaultChainLinkParser extends AbstractChainLinkParser {

        @Override
        protected Result doInternalParse(String link) {
            return Result.failed();
        }
    }
}
