package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BotConfiguration {

    @Bean
    public TelegramBot bot(@Value("${telegram.bot.access_token}") String botToken,
                           ApplicationContext applicationContext) {

        final var bot = new TelegramBot(botToken);

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                System.out.println("main handler");
                this.handle(bot, update);
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        return bot;
    }

    private void handle(TelegramBot bot, Update update) {

        final var chatId = update.message().chat().id();
        final var request = new SendMessage(chatId, "First");

        bot.execute(request, new Callback<SendMessage, SendResponse>() {

            @Override
            public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                final var caption = sendResponse.message().caption();
                bot.execute(new SendMessage(chatId, "Caption: " + caption));
            }

            @Override
            public void onFailure(SendMessage sendMessage, IOException e) {

            }
        });
    }
}
