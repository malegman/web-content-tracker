package ru.tinkoff.edu.java.scrapper.application.shared.application.spi;

import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.GitHubUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

/**
 * Компонент для добавления отслеживаемой ссылки GitHub в чат телеграмма
 */
@FunctionalInterface
public interface AddLinkGitHubSpi {

    /**
     * Метод для добавления отслеживаемой ссылки GitHub в чат телеграмма
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
    TgChatLinkId addLinkGitHub(TgChatId tgChatId, String link, GitHubUpdatesDto dto)
            throws TgChatNotExistsException, TgChatLinkAlreadyExistsException;
}
