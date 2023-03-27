package ru.tinkoff.edu.java.scrapper.application.tgchat.api.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.tinkoff.edu.java.scrapper.common.ApiErrorResponseAssertionMatcher;
import ru.tinkoff.edu.java.scrapper.common.errors.ErrorType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTgChatHandlerFunctionIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void registerTgChat_RequestIsValid_ReturnsResponseWithStatusNoContent() throws Exception {

        final var requestBuilder = post("/tg-chat/1");

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    void registerTgChat_IncorrectInput_ReturnsResponseWithStatusBadRequest() throws Exception {

        final var requestBuilder = post("/tg-chat/id");

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
