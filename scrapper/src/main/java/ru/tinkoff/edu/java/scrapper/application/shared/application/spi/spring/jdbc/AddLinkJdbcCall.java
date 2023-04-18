package ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.GitHubUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.StackOverflowUpdatesDto;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkGitHubSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkStackOverflowSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.LinkType;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.scrapper.application.shared.domain.id.TgChatLinkId;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;
import java.util.Objects;

/**
 * Реализация {@link AddLinkGitHubSpi}, с использованием {@link SimpleJdbcCall}
 */
public final class AddLinkJdbcCall extends SimpleJdbcCall
        implements AddLinkGitHubSpi, AddLinkStackOverflowSpi {

    private static final String SQL_SCHEMA = "scrapper";
    private static final String SQL_FUNCTION = "set_tg_chat_link";

    private static final String PARAMETER_LINK = "_link";
    private static final String PARAMETER_LINK_TYPE_NAME = "_link_type_name";
    private static final String PARAMETER_LINK_DATA = "_link_data";
    private static final String PARAMETER_ID_TG_CHAT = "_id_tg_chat";

    private final ObjectMapper objectMapper;

    public AddLinkJdbcCall(final DataSource dataSource,
                           final ObjectMapper objectMapper) {
        super(dataSource);
        this.objectMapper = Objects.requireNonNull(objectMapper);

        this.withSchemaName(SQL_SCHEMA)
                .withFunctionName(SQL_FUNCTION)
                .declareParameters(
                        new SqlParameter(PARAMETER_LINK, Types.VARCHAR),
                        new SqlParameter(PARAMETER_LINK_TYPE_NAME, Types.VARCHAR),
                        new SqlParameter(PARAMETER_LINK_DATA, Types.VARCHAR),
                        new SqlParameter(PARAMETER_ID_TG_CHAT, Types.BIGINT))
                .compile();
    }

    @Override
    public TgChatLinkId addLinkGitHub(TgChatId tgChatId, String link, GitHubUpdatesDto dto) {
        try {
            return TgChatLinkId.valueOf(
                    this.executeFunction(Long.class, Map.of(
                            PARAMETER_LINK, link,
                            PARAMETER_LINK_TYPE_NAME, LinkType.GITHUB.getValue(),
                            PARAMETER_LINK_DATA, this.objectMapper.writeValueAsString(dto),
                            PARAMETER_ID_TG_CHAT, tgChatId.value()
                    )));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TgChatLinkId addLinkStackOverflow(TgChatId tgChatId, String link, StackOverflowUpdatesDto dto) {
        try {
            return TgChatLinkId.valueOf(
                    this.executeFunction(Long.class, Map.of(
                            PARAMETER_LINK, link,
                            PARAMETER_LINK_TYPE_NAME, LinkType.STACKOVERFLOW.getValue(),
                            PARAMETER_LINK_DATA, this.objectMapper.writeValueAsString(dto),
                            PARAMETER_ID_TG_CHAT, tgChatId.value()
                    )));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
