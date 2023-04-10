package ru.tinkoff.edu.java.bot.common.bot.handler.command;

import org.reflections.Reflections;
import org.springframework.beans.factory.BeanFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Ищет наследников класса {@link CommandInnerHandler}. Из найденных наследников подготавливает обработчиков команд.
 */
public class CommandInnerHandlerAnalyzer {

    public Map<String, Queue<CommandInnerHandler>> findCommandQueueInnerHandlers(final BeanFactory beanFactory) {

        final var mapCommandListHandlers = findCommandInnerHandlers()
                .stream()
                .map(clazz -> this.getInstanceFromClass(clazz, beanFactory))
                .collect(Collectors.groupingBy(CommandInnerHandler::getCommand));

        return mapCommandListHandlers.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> this.queueFromCommandInnerHandlerList(entry.getValue())));
    }

    /**
     * Преобразует список обработчиков в очередь, с учетом порядка обработчика
     * @param list список обработчиков
     *
     * @return собранная очередь обработчиков
     */
    private Queue<CommandInnerHandler> queueFromCommandInnerHandlerList(List<CommandInnerHandler> list) {
        return new PriorityQueue<>(list
                .stream()
                .sorted(Comparator.comparing(CommandInnerHandler::getOrder))
                .toList());
    }

    /**
     * Находит наследников класса {@link CommandInnerHandler}
     *
     * @return коллекция наследников
     */
    private Collection<Class<? extends CommandInnerHandler>> findCommandInnerHandlers() {

        final var reflections = new Reflections("ru.tinkoff.edu.java.bot");
        return reflections.getSubTypesOf(CommandInnerHandler.class);
    }

    /**
     * Возвращает экземпляр класса-обработчика {@link CommandInnerHandler}
     * @param clazz       класс обработчика
     * @param beanFactory фабрика бинов для инициализации обработчика
     *
     * @return экземпляр обработчика
     */
    private CommandInnerHandler getInstanceFromClass(final Class<? extends CommandInnerHandler> clazz, final BeanFactory beanFactory) {

        return Arrays.stream(clazz.getConstructors())
                .findFirst()
                .map(constructor -> {
                    try {
                        final var argsType = Arrays.stream(constructor.getParameterTypes())
                                .map(beanFactory::getBean)
                                .toArray();
                        return (CommandInnerHandler)constructor.newInstance(argsType);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow();
    }
}
