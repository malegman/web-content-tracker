package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

/**
 * Реализация {@link DeleteTgChatSpi}, с использованием {@link DeleteTgChatSpi}
 */
public final class DeleteTgChatJdbcCall extends SimpleJdbcCall implements DeleteTgChatSpi {

    private static final String SQL_SCHEMA = "scrapper";
    private static final String SQL_FUNCTION = "del_tg_chat";

    private static final String PARAMETER_ID_TG_CHAT = "_id_tg_chat";

    public DeleteTgChatJdbcCall(final DataSource dataSource) {
        super(dataSource);

        this.withSchemaName(SQL_SCHEMA)
                .withFunctionName(SQL_FUNCTION)
                .declareParameters(
                        new SqlParameter(PARAMETER_ID_TG_CHAT, Types.BIGINT))
                .compile();
    }

    @Override
    public void deleteTgChat(TgChatId tgChatId) throws TgChatNotExistsException {
        try {
            this.executeFunction(Long.class, Map.of(
                    PARAMETER_ID_TG_CHAT, tgChatId.value()));
        } catch (DataAccessException e) {
            final var message = e.getMessage();
            if (message.contains("Tg chat doesn't exist.")) {
                throw new TgChatNotExistsException();
            }
            throw e;
        }
    }
}
