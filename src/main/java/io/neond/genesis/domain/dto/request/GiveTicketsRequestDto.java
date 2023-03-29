package io.neond.genesis.domain.dto.request;

import lombok.Getter;

@Getter
public class GiveTicketsRequestDto {
    private String to;
    private String type;
    private int amount;
}
