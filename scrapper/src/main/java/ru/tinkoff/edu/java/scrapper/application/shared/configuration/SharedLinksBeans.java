package ru.tinkoff.edu.java.scrapper.application.shared.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkGitHubSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.AddLinkStackOverflowSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteLinkSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.FindLinksSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc.AddLinkJdbcCall;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc.DeleteLinkJdbcCall;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc.FindLinksJdbcCall;

import javax.sql.DataSource;

@Configuration
public class SharedLinksBeans {

    @Bean
    public FindLinksSpi findLinksSpi(DataSource dataSource) {
        return new FindLinksJdbcCall(dataSource);
    }

    @Bean
    public AddLinkGitHubSpi addLinkGitHubSpi(DataSource dataSource,
                                             ObjectMapper objectMapper) {
        return new AddLinkJdbcCall(dataSource, objectMapper);
    }

    @Bean
    public AddLinkStackOverflowSpi addLinkStackOverflowSpi(DataSource dataSource,
                                                           ObjectMapper objectMapper) {
        return new AddLinkJdbcCall(dataSource, objectMapper);
    }

    @Bean
    public DeleteLinkSpi deleteLinkSpi(DataSource dataSource) {
        return new DeleteLinkJdbcCall(dataSource);
    }
}
