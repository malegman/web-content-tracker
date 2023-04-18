package ru.tinkoff.edu.java.scrapper.application.tgchat.api.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.common.ApiErrorResponseAssertionMatcher;
import ru.tinkoff.edu.java.scrapper.common.errors.ErrorType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class DeleteTgChatHandlerFunctionIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void deleteTgChat_RequestIsValid_ReturnsResponseWithStatusNoContent() throws Exception {

        final var requestBuilder = delete("/tg-chat/1");

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTgChat_IncorrectInput_ReturnsResponseWithStatusBadRequest() throws Exception {

        final var requestBuilder = delete("/tg-chat/id");

        final var matcher = new ApiErrorResponseAssertionMatcher(this.objectMapper);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(matcher));

        final var apiErrorResponse = matcher.getApiErrorResponse();
        Assertions.assertNotNull(apiErrorResponse);
        Assertions.assertEquals(apiErrorResponse.description(), ErrorType.VALIDATION_FAILED.getDescription());
        Assertions.assertEquals(apiErrorResponse.code(), ErrorType.VALIDATION_FAILED.getCode());
    }
}
