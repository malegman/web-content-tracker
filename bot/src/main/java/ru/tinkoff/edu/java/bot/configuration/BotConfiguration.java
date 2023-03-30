package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.telegram.bot.HandlerManager;
import ru.tinkoff.edu.java.bot.common.telegram.bot.RouterCommand;

import java.io.IOException;
import java.util.stream.Collectors;

@Configuration
public class BotConfiguration {

    @Bean
    public HandlerManager handlerManager(ApplicationContext applicationContext) {

        final var commandHandlers = applicationContext
                .getBeansOfType(RouterCommand.class).values()
                .stream()
                .collect(Collectors.toMap(
                        RouterCommand::getCommand,
                        RouterCommand::getHandler));

        return new HandlerManager(commandHandlers);
    }

    @Bean
    public TelegramBot bot(@Value("${telegram.bot.access_token}") String botToken,
                           final HandlerManager handlerManager) {

        final var bot = new TelegramBot(botToken);

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                final var message = update.message();
                final var handler = handlerManager.getHandler(TgChatId.valueOf(message.chat().id()), message.text());
                bot.execute(handler.handle(update), new Callback<SendMessage, SendResponse>() {
                    @Override
                    public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                    }

                    @Override
                    public void onFailure(SendMessage sendMessage, IOException e) {
                    }
                });
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        return bot;
    }
}
