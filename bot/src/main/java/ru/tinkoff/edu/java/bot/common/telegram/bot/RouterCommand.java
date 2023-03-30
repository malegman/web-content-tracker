package ru.tinkoff.edu.java.bot.common.telegram.bot;

public interface RouterCommand {

    String getCommand();

    HandlerCommand getHandler();

    Builder builder();

    abstract class Builder {

        protected String command;
        protected HandlerCommand handlerCommand;

        public Builder command(String value) {
            this.command = value;
            return this;
        }

        public Builder handler(HandlerCommand value) {
            this.handlerCommand = value;
            return this;
        }

        public abstract RouterCommand build();
    }
}
