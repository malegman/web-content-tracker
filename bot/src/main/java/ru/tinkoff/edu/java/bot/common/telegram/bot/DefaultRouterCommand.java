package ru.tinkoff.edu.java.bot.common.telegram.bot;

class DefaultRouterCommand implements RouterCommand {

    private final String command;
    private final HandlerCommand handlerCommand;

    private DefaultRouterCommand(Builder builder) {
        this.command = builder.command;
        this.handlerCommand = builder.handlerCommand;
    }

    @Override
    public String getCommand() {
        return this.command;
    }

    @Override
    public HandlerCommand getHandler() {
        return this.handlerCommand;
    }

    @Override
    public Builder builder() {
        return new Builder();
    }

    static class Builder extends RouterCommand.Builder {

        @Override
        public RouterCommand build() {
            return new DefaultRouterCommand(this);
        }
    }
}
