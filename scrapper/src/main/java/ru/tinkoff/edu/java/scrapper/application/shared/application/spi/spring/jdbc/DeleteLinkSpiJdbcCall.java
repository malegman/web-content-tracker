package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteLinkSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkType;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;

/**
 * Реализация {@link DeleteLinkSpi}, с использованием {@link DeleteLinkSpi}
 */
public final class DeleteLinkSpiJdbcCall extends SimpleJdbcCall implements DeleteLinkSpi {

    private static final String SQL_SCHEMA = "scrapper";
    private static final String SQL_FUNCTION = "del_tg_chat_link";

    private static final String PARAMETER_LINK = "_link";
    private static final String PARAMETER_ID_TG_CHAT = "_id_tg_chat";

    public DeleteLinkSpiJdbcCall(final DataSource dataSource,
                           final ObjectMapper objectMapper) {
        super(dataSource);

        this.withSchemaName(SQL_SCHEMA)
                .withFunctionName(SQL_FUNCTION)
                .declareParameters(
                        new SqlParameter(PARAMETER_LINK, Types.VARCHAR),
                        new SqlParameter(PARAMETER_ID_TG_CHAT, Types.BIGINT))
                .compile();
    }

    @Override
    public TgChatLinkId deleteLink(TgChatId tgChatId, String link) {
        try {
            return TgChatLinkId.valueOf(
                    this.executeFunction(Long.class, Map.of(
                            PARAMETER_LINK, link,
                            PARAMETER_ID_TG_CHAT, tgChatId.value()
                    )));
        } catch (DataAccessException e) {
            final var message = e.getMessage();
            if (message.contains("Link doesn't exist.") || message.contains("This tg chat hasn't got that link.")) {
                throw new TgChatLinkNotExistsException();
            } else if (message.contains("Tg chat doesn't exist.")) {
                throw new TgChatNotExistsException();
            }
            throw e;
        }
    }
}
