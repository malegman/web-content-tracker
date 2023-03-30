package ru.tinkoff.edu.java.bot.application.client.configuration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.*;
import ru.tinkoff.edu.java.bot.application.client.api.spring.web.SendUpdatesHandlerFunction;
import ru.tinkoff.edu.java.bot.application.client.usecase.SendUpdatesUseCase;
import ru.tinkoff.edu.java.bot.application.shared.application.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.application.shared.application.spi.SendUpdatesSpi;
import ru.tinkoff.edu.java.bot.common.api.errors.ApiErrorResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class BotLinksBeans {

    @Bean
    @RouterOperation(operation = @Operation(
            tags = {"links"},
            operationId = "addLink",
            summary = "Отправить обновление",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LinkUpdateRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Обновление обработано"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные параметры запроса",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApiErrorResponse.class)))}))
    public RouterFunction<ServerResponse> sendUpdatesRouterFunction(
            final SendUpdatesSpi sendUpdatesSpi) {

        final var handlerFunction = new SendUpdatesHandlerFunction(
                new SendUpdatesUseCase(sendUpdatesSpi));

        return route()
                .POST("/updates",
                        RequestPredicates.contentType(MediaType.APPLICATION_JSON),
                        handlerFunction)
                .build();
    }
}
