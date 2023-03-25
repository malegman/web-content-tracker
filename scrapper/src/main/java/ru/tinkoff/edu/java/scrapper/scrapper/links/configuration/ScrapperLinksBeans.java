package ru.tinkoff.edu.java.scrapper.scrapper.links.configuration;

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
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import ru.tinkoff.edu.java.scrapper.common.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.scrapper.links.api.spring.web.FindLinksHandlerFunction;
import ru.tinkoff.edu.java.scrapper.scrapper.links.usecase.FindLinksUseCase;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.dto.response.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.scrapper.shared.application.spi.FindLinksSpi;

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
                                    schema = @Schema(implementation = ListLinksResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса",
                            content = @Content(
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
                .POST("/links",
                        handlerFunction)
                .build();
    }
}
