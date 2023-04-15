package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import com.pengrad.telegrambot.request.SendMessage;
import ru.tinkoff.edu.java.bot.application.shared.domain.id.TgChatId;
import ru.tinkoff.edu.java.bot.common.bot.BotRequest;

import java.util.function.Consumer;

/**
 * Интерфейс внутреннего обработчика команды
 */
public interface CommandInnerHandler extends Comparable<CommandInnerHandler> {

    /**
     * Возвращает порядок обработчика в команде
     */
    int getOrder();

    /**
     * Возвращает обрабатываемую команду
     */
    String getCommand();

    /**
     * Обрабатывает запрос бота.
     *
     * @param botRequest запрос бота
     *
     * @return результат обработки
     */
    Result innerHandle(BotRequest botRequest);

    @Override
    default int compareTo(CommandInnerHandler o) {
        return 0;
    }

    /**
     * Результат обработки сообщения внутри команды
     *
     * @param sendMessage сообщение для отправки пользователю
     * @param resultType  тип результата
     */
    record Result(SendMessage sendMessage, ResultType resultType) {

        public static Builder sendMessage(TgChatId tgChatId, String text) {
            return new Builder(tgChatId, text);
        }

        enum ResultType {
            SUCCESS,
            REPEAT,
            ABORT
        }

        public boolean isSuccess() {
            return this.resultType.equals(ResultType.SUCCESS);
        }

        public boolean isAbort() {
            return this.resultType.equals(ResultType.ABORT);
        }

        public boolean isRepeat() {
            return this.resultType.equals(ResultType.REPEAT);
        }

        public static final class Builder {

            private final SendMessage sendMessage;

            public Builder(TgChatId tgChatId, String text) {
                this.sendMessage = new SendMessage(tgChatId.value(), text);
            }

            public Builder modifySendMessage(Consumer<SendMessage> sendMessageConsumer) {
                sendMessageConsumer.accept(this.sendMessage);
                return this;
            }

            public Result success() {
                return new Result(this.sendMessage, ResultType.SUCCESS);
            }

            public Result repeat() {
                return new Result(this.sendMessage, ResultType.REPEAT);
            }

            public Result abort() {
                return new Result(this.sendMessage, ResultType.ABORT);
            }
        }
    }
}
