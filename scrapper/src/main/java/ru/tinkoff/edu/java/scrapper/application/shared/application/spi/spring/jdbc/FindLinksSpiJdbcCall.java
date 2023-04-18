package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.TgChatLinkDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindLinksSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkAlreadyExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatLinkNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.exception.TgChatNotExistsException;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkType;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Реализация {@link FindLinksSpi}, с использованием {@link MappingSqlQuery}
 */
public final class FindLinksSpiJdbcCall extends MappingSqlQuery<TgChatLinkDto> implements FindLinksSpi {

    private static final String SQL_QUERY =
            "SELECT * FROM scrapper.get_tg_chat_link(:_id_tg_chat);";

    private static final String PARAMETER_ID_TG_CHAT = "_id_tg_chat";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LINK = "c_link";
    private static final String COLUMN_NAME = "c_name";

    public FindLinksSpiJdbcCall(DataSource dataSource) {
        super(dataSource, SQL_QUERY);
        this.declareParameter(new SqlParameter(PARAMETER_ID_TG_CHAT, Types.BIGINT));
        this.compile();
    }

    @Override
    protected TgChatLinkDto mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new TgChatLinkDto(
                new TgChatLinkId(resultSet.getLong(COLUMN_ID)),
                resultSet.getString(COLUMN_LINK),
                LinkType.valueOf(resultSet.getString(COLUMN_NAME)));
    }

    @Override
    public List<TgChatLinkDto> findLinks(TgChatId tgChatId) throws TgChatLinkNotExistsException {
        try {
            return this.executeByNamedParam(Map.of(
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
