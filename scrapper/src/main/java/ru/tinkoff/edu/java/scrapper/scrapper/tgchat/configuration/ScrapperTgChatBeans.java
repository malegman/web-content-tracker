package ru.tinkoff.edu.java.scrapper.scrapper.tgchat.configuration;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.RegisterTgChatSpi;
import ru.tinkoff.edu.java.scrapper.scrapper.tgchat.api.spring.web.RegisterTgChatHandlerFunction;
import ru.tinkoff.edu.java.scrapper.scrapper.tgchat.usecase.RegisterTgChatUseCase;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class ScrapperTgChatBeans {

    @Bean
    public RouterFunction<ServerResponse> registerTgChatRouterFunction(
            final BeanFactory beanFactory,
            final RegisterTgChatSpi registerTgChatSpi) {

        final var handlerFunction = new RegisterTgChatHandlerFunction(
                new RegisterTgChatUseCase(registerTgChatSpi));
        handlerFunction.setBeanFactory(beanFactory);

        return route()
                .POST("/tg-chat/{id}",
                        handlerFunction)
                .build();
    }
}
