package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.RegisterTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

/**
 * Реализация {@link RegisterTgChatSpi}, с использованием {@link SimpleJdbcCall}
 */
public final class RegisterTgChatJdbcCall extends SimpleJdbcCall implements RegisterTgChatSpi {

    private static final String SQL_SCHEMA = "scrapper";
    private static final String SQL_FUNCTION = "set_tg_chat";

    private static final String PARAMETER_ID_TG_CHAT = "_id_tg_chat";

    public RegisterTgChatJdbcCall(final DataSource dataSource) {
        super(dataSource);

        this.withSchemaName(SQL_SCHEMA)
                .withFunctionName(SQL_FUNCTION)
                .declareParameters(
                        new SqlParameter(PARAMETER_ID_TG_CHAT, Types.BIGINT))
                .compile();
    }

    @Override
    public void registerTgChat(TgChatId tgChatId) throws TgChatAlreadyExistsException {
        try {
            this.executeFunction(Long.class, Map.of(
                    PARAMETER_ID_TG_CHAT, tgChatId.value()));
        } catch (DataAccessException e) {
            final var message = e.getMessage();
            if (message.contains("t_tg_chat")) {
                throw new TgChatAlreadyExistsException();
            }
            throw e;
        }
    }
}
