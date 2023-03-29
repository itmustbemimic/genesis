package io.neond.genesis.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyTicketResponseDto {
    private int black;
    private int red;
    private int gold;
}
