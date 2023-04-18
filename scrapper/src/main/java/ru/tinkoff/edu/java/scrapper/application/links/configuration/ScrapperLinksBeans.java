package ru.tinkoff.edu.java.scrapper.application.links.configuration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.application.spi.*;
import ru.tinkoff.edu.java.scrapper.common.errors.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.application.links.api.spring.web.AddLinkHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.links.api.spring.web.DeleteLinkHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.links.api.spring.web.FindLinksHandlerFunction;
import ru.tinkoff.edu.java.scrapper.application.links.usecase.AddLinkUseCase;
import ru.tinkoff.edu.java.scrapper.application.links.usecase.DeleteLinkUseCase;
import ru.tinkoff.edu.java.scrapper.application.links.usecase.FindLinksUseCase;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.application.shared.application.dto.response.ListLinksResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class ScrapperLinksBeans {

    @Bean
    @RouterOperation(operation = @Operation(
            tags = {"links"},
            operationId = "getLinks",
            summary = "Получить все отслеживаемые ссылки",
            parameters = {
                    @Parameter(
                            name = "Tg-Chat-Id",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Идентификатор чата",
                            schema = @Schema(implementation = Long.class))},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ссылки успешно получены",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ListLinksResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Чат не существует")}))
    public RouterFunction<ServerResponse> findLinksRouterFunction(
            final BeanFactory beanFactory,
            final FindLinksSpi findLinksSpi) {

        final var handlerFunction = new FindLinksHandlerFunction(
                new FindLinksUseCase(findLinksSpi));
        handlerFunction.setBeanFactory(beanFactory);

        return route()
                .GET("/links",
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        handlerFunction)
                .build();
    }

    @Bean
    @RouterOperation(operation = @Operation(
            tags = {"links"},
            operationId = "addLink",
            summary = "Добавить отслеживание ссылки",
            parameters = {
                    @Parameter(
                            name = "Tg-Chat-Id",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Идентификатор чата",
                            schema = @Schema(implementation = Long.class))},
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AddLinkRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ссылка успешно добавлена",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ListLinksResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Чат или ссылка не существует"),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Ссылка уже отслеживается")}))
    public RouterFunction<ServerResponse> addLinkRouterFunction(
            final BeanFactory beanFactory,
            final AddLinkGitHubSpi addLinkGitHubSpi,
            final AddLinkStackOverflowSpi addLinkStackOverflowSpi,
            final FindGitHubUpdatesSpi findGitHubUpdatesSpi,
            final FindStackOverflowUpdatesSpi findStackOverflowUpdatesSpi) {

        final var handlerFunction = new AddLinkHandlerFunction(
                new AddLinkUseCase(
                        addLinkGitHubSpi,
                        addLinkStackOverflowSpi,
                        findGitHubUpdatesSpi,
                        findStackOverflowUpdatesSpi));
        handlerFunction.setBeanFactory(beanFactory);

        return route()
                .POST("/links",
                        RequestPredicates.contentType(MediaType.APPLICATION_JSON)
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                        handlerFunction)
                .build();
    }

    @Bean
    @RouterOperation(operation = @Operation(
            tags = {"links"},
            operationId = "deleteLink",
            summary = "Убрать отслеживание ссылки",
            parameters = {
                    @Parameter(
                            name = "Tg-Chat-Id",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Идентификатор чата",
                            schema = @Schema(implementation = Long.class)),
                    @Parameter(
                            name = "url",
                            in = ParameterIn.QUERY,
                            description = "Ссылка для удаления из списка отслеживания",
                            schema = @Schema(implementation = String.class))},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ссылка успешно убрана",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LinkResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Чат не существует"),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Ссылка уже отслеживается")}))
    public RouterFunction<ServerResponse> deleteLinkRouterFunction(
            final BeanFactory beanFactory,
            final DeleteLinkSpi deleteLinkSpi) {

        final var handlerFunction = new DeleteLinkHandlerFunction(
                new DeleteLinkUseCase(deleteLinkSpi));
        handlerFunction.setBeanFactory(beanFactory);

        return route()
                .DELETE("/links",
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        handlerFunction)
                .build();
    }
}
