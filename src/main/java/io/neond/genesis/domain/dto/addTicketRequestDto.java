package io.neond.genesis.domain.dto;

import io.neond.genesis.domain.entity.Ticket;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class addTicketRequestDto {
    private String memberId;
    private int blackAmount;
    private int redAmount;
    private int goldAmount;
}
