package io.neond.genesis.domain.dto.request;

import lombok.*;
@Getter
public class SmsMessageDto {
    String to;
    String authKey;
}
