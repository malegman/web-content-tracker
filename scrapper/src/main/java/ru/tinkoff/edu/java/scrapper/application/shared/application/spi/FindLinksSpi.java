package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

import java.util.List;

/**
 * Компонент для получения отслеживаемых ссылок чата телеграмма
 */
@FunctionalInterface
public interface FindLinksSpi {

    /**
     * Метод для получения отслеживаемых ссылок чата телеграмма
     *
     * @param tgChatId идентификатор чата телеграмма
     *
     * @return список ссылок, не может быть {@code null}
     *
     * @throws TgChatLinkNotExistsException чат не найден
     */
    List<TgChatLinkDto> findLinks(TgChatId tgChatId)
            throws TgChatLinkNotExistsException;
}
