package io.neond.genesis.domain.dto.response;

import java.time.LocalDateTime;

public class SmsResponseDto {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}
