package ru.tinkoff.edu.java.bot.application.shared.application.dto.response;

import java.util.List;

public record ListLinkScrapperResponse(List<LinkScrapperResponse> links, Integer size) {
}
