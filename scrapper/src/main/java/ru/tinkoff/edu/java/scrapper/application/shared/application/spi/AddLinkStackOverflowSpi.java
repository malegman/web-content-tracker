package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.StackOverflowUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

/**
 * Компонент для добавления отслеживаемой ссылки StackOverflow в чат телеграмма
 */
@FunctionalInterface
public interface AddLinkStackOverflowSpi {

    /**
     * Метод для добавления отслеживаемой ссылки StackOverflow в чат телеграмма
     *
     * @param tgChatId идентификатор чата
     * @param link     ссылка
     * @param dto      данные, отслеживаемые по ссылке
     *
     * @return идентификатор добавленной ссылки
     *
     * @throws TgChatNotExistsException         чат телеграма не существует
     * @throws TgChatLinkAlreadyExistsException данная ссылка уже привязана к чату
     */
    TgChatLinkId addLinkStackOverflow(TgChatId tgChatId, String link, StackOverflowUpdatesDto dto)
            throws TgChatNotExistsException, TgChatLinkAlreadyExistsException;
}
