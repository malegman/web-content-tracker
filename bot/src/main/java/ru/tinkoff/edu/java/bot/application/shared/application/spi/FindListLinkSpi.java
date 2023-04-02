package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.ListLinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

@FunctionalInterface
public interface FindListLinkSpi {

    WebClientBodyResponse<ListLinkScrapperResponse> findLinks(TgChatId tgChatId);
}
