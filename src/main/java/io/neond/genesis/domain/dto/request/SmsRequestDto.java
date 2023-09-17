package io.neond.genesis.domain.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public class SmsRequestDto {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<SmsMessageDto> messageDtoList;
}
