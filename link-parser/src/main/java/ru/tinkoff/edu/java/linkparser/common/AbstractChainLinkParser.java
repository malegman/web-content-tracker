package ru.tinkoff.edu.java.linkparser.common;

import java.util.Objects;

/**
 * Абстрактный парсер ссылок
 */
public abstract class AbstractChainLinkParser implements LinkParser {

    private AbstractChainLinkParser next;

    public void setNext(final AbstractChainLinkParser next) {
        this.next = Objects.requireNonNull(next);
    }

    protected abstract Result doInternalParse(String link);

    @Override
    public Result doParse(final String link) {

        final var result = this.doInternalParse(link);
        if (result.isSuccess()) {
            return result;
        }

        if (this.next == null) {
            return Result.failed();
        } else {
            return this.next.doParse(link);
        }
    }
}
