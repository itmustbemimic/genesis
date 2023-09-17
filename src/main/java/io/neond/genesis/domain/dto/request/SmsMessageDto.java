package io.neond.genesis.domain.dto.request;

import lombok.Getter;

@Getter
public class SmsMessageDto {
    String to;
    String content;
}
