package ru.tinkoff.edu.java.scrapper.application.shared.domain.id;

/**
 * Тип ссылки
 */
public enum LinkType {
    GITHUB("GITHUB"),
    STACKOVERFLOW("STACKOVERFLOW");

    private final String value;

    LinkType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
