package ru.tinkoff.edu.java.bot.application.shared.application.spi;

import ru.tinkoff.edu.java.bot.application.shared.application.dto.response.LinkScrapperResponse;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.web.WebClientBodyResponse;

@FunctionalInterface
public interface DeleteLinkSpi {

    WebClientBodyResponse<LinkScrapperResponse> deleteLink(TgChatId tgChatId, String link);
}
