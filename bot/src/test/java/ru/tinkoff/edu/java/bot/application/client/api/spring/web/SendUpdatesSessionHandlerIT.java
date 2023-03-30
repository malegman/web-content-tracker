package ru.tinkoff.edu.java.bot.application.client.api.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.edu.java.bot.common.ApiErrorResponseAssertionMatcher;
import ru.tinkoff.edu.java.bot.common.api.errors.ErrorType;
import ru.tinkoff.edu.java.bot.common.api.errors.ValidationFailedException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SendUpdatesSessionHandlerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void sendUpdates_RequestIsValid_ReturnsResponseWithStatusNoContent() throws Exception {

        final var requestBuilder = post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": 1,
                            "url": "url",
                            "description": "description",
                            "tgChatIds": [1, 2]
                        }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    void sendUpdates_RequestIsInvalid_ReturnsResponseWithStatusBadRequest() throws Exception {

        final var requestBuilder = post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "url": " ",
                            "tgChatIds": [1, null]
                        }
                        """);

        final var matcher = new ApiErrorResponseAssertionMatcher(this.objectMapper);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matcher));

        final var apiErrorResponse = matcher.getApiErrorResponse();
        Assertions.assertNotNull(apiErrorResponse);
        Assertions.assertEquals(apiErrorResponse.description(), ErrorType.VALIDATION_FAILED.getDescription());
        Assertions.assertEquals(apiErrorResponse.code(), ErrorType.VALIDATION_FAILED.getCode());
        Assertions.assertEquals(4, apiErrorResponse.exceptionMessage().split(";").length);
        Assertions.assertEquals(apiErrorResponse.exceptionName(), ValidationFailedException.class.getName());
    }

    @Test
    void sendUpdates_IncorrectInput_ReturnsResponseWithStatusBadRequest() throws Exception {

        final var requestBuilder = post("/updates")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id": "id",
                            "url": "url",
                            "description": "description",
                            "tgChatIds": [1, 2]
                        }
                        """);

        final var matcher = new ApiErrorResponseAssertionMatcher(this.objectMapper);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matcher));

        final var apiErrorResponse = matcher.getApiErrorResponse();
        Assertions.assertNotNull(apiErrorResponse);
        Assertions.assertEquals(apiErrorResponse.description(), ErrorType.EXECUTION_FAILED.getDescription());
        Assertions.assertEquals(apiErrorResponse.code(), ErrorType.EXECUTION_FAILED.getCode());
    }
}
