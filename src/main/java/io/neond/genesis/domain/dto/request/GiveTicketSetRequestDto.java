package io.neond.genesis.domain.dto.request;

import lombok.Getter;

@Getter
public class GiveTicketSetRequestDto {
    private String to;
    private int redAmount;
    private int blackAmount;
    private int goldAmount;
}
