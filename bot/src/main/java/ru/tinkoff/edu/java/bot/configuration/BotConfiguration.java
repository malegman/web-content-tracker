package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotUtils;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandHandler;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandHandlerFactory;
import ru.tinkoff.edu.java.bot.common.bot.handler.command.CommandHandlerManager;

import java.io.IOException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class BotConfiguration {

    @Bean
    public CommandHandlerManager handlerManager(ApplicationContext applicationContext) {

        final var commandHandlers = applicationContext
                .getBeansOfType(CommandHandlerFactory.class).values()
                .stream()
                .collect(Collectors.toMap(
                        CommandHandlerFactory::getCommand,
                        Function.<Function<CommandHandlerManager, CommandHandler>>identity()));

        return new CommandHandlerManager(commandHandlers);
    }

    @Bean
    public TelegramBot bot(@Value("${telegram.bot.access_token}") String botToken,
                           final CommandHandlerManager commandHandlerManager) {

        final var bot = new TelegramBot(botToken);

        bot.setUpdatesListener(updates -> {
            updates.forEach(update -> {
                try {
                    final var botRequest = BotUtils.requestFromUpdate(update);
                    final var handler = commandHandlerManager.getHandler(
                            botRequest.tgChatId(), botRequest.messageText());

                    bot.execute(handler.handle(botRequest), new Callback<SendMessage, SendResponse>() {
                        @Override
                        public void onResponse(SendMessage sendMessage, SendResponse sendResponse) {
                        }

                        @Override
                        public void onFailure(SendMessage sendMessage, IOException e) {
                        }
                    });
                } catch (Exception exception) {
                    log.error("Error", exception);
                }
            });
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        return bot;
    }
}
