package ru.tinkoff.edu.java.scrapper.application.links.api.spring.web;

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
import ru.tinkoff.edu.java.scrapper.common.errors.ValidationFailedException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AddLinkHandlerFunctionIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addLink_RequestIsValid_ReturnsResponseWithStatusOk() throws Exception {

        final var requestBuilder = post("/links")
                .header("Tg-Chat-Id", "1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "link": "url"
                        }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {}
                        """));
    }

    @Test
    void addLink_RequestIsInvalid_ReturnsResponseWithStatusBadRequest() throws Exception {

        final var requestBuilder = post("/links")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "link": " "
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
        Assertions.assertEquals(2, apiErrorResponse.exceptionMessage().split(";").length);
        Assertions.assertEquals(apiErrorResponse.exceptionName(), ValidationFailedException.class.getName());
    }

    @Test
    void addLink_IncorrectInput_ReturnsResponseWithStatusBadRequest() throws Exception {

        final var requestBuilder = post("/links")
                .header("Tg-Chat-Id", "id")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "link": "url"
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
