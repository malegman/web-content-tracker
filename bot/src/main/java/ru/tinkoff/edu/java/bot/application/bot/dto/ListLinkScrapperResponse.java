package ru.tinkoff.edu.java.bot.application.bot.dto;

import java.util.List;

public record ListLinkScrapperResponse(List<LinkScrapperResponse> links, Integer size) {
}
