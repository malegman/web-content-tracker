package ru.tinkoff.edu.java.scrapper.application.shared.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.RegisterTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc.DeleteTgChatJdbcCall;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.spring.jdbc.RegisterTgChatJdbcCall;

import javax.sql.DataSource;

@Configuration
public class SharedTgChatBeans {

    @Bean
    public RegisterTgChatSpi registerTgChatSpi(DataSource dataSource) {
        return new RegisterTgChatJdbcCall(dataSource);
    }

    @Bean
    public DeleteTgChatSpi deleteTgChatSpi(DataSource dataSource) {
        return new DeleteTgChatJdbcCall(dataSource);
    }
}
