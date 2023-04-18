package ru.tinkoff.edu.java.scrapper.application.tgchat.configuration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.common.errors.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.DeleteTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.RegisterTgChatSpi;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.spring.web.DeleteTgChatHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.tgchat.api.spring.web.RegisterTgChatHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.tgchat.usecase.DeleteTgChatUseCase;
import ru.tinkoff.edu.java.scrapper.application.tgchat.usecase.RegisterTgChatUseCase;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class ScrapperTgChatBeans {

    @Bean
    @RouterOperation(operation = @Operation(
            tags = {"tg-chat"},
            operationId = "registerTgChat",
            summary = "Зарегистрировать чат",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "Идентификатор чата",
                            schema = @Schema(implementation = Long.class))},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Чат зарегистрирован"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Чат уже зарегистрирован")}))
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

    @Bean
    @RouterOperation(operation = @Operation(
            tags = {"tg-chat"},
            operationId = "deleteTgChat",
            summary = "Удалить чат",
            parameters = {
                    @Parameter(
                            name = "id",
                            in = ParameterIn.PATH,
                            required = true,
                            description = "Идентификатор чата",
                            schema = @Schema(implementation = Long.class))},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Чат успешно удалён"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)))}))
    public RouterFunction<ServerResponse> deleteTgChatRouterFunction(
            final BeanFactory beanFactory,
            final DeleteTgChatSpi deleteTgChatSpi) {

        final var handlerFunction = new DeleteTgChatHandlerFunction(
                new DeleteTgChatUseCase(deleteTgChatSpi));
        handlerFunction.setBeanFactory(beanFactory);

        return route()
                .DELETE("/tg-chat/{id}",
                        handlerFunction)
                .build();
    }
}
