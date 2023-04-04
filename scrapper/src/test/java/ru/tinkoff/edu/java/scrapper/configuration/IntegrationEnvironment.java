package ru.tinkoff.edu.java.scrapper.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@Profile("standalone")
@Slf4j
public class IntegrationEnvironment {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public DockerComposeContainer<?> postgresContainer() {
        return new DockerComposeContainer<>(new File("src/main/resources/static/db/docker-compose.yml"))
                .withLogConsumer("postgresql", outputFrame -> log.info(outputFrame.getUtf8String()))
                .waitingFor("postgresql", Wait.forLogMessage(".*database system is ready to accept connections.*", 2));
    }

    @Bean
    public DataSource dataSource(@Qualifier("postgresContainer") DockerComposeContainer<?> postgresContainer,
                                 @Value("${spring.datasource.username:}") String username,
                                 @Value("${spring.datasource.password:}") String password,
                                 @Value("${docker.pg_port:}") int pgPort) {
        final var postgresContainerHost = postgresContainer.getServiceHost("postgresql", pgPort);
        final var config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://" +
                postgresContainerHost + ":" + pgPort + "/scrapper");
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:static/db/migrations/master.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
