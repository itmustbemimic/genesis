package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class IssuedResponseDto {
    List<TicketSet> userBuy;
    List<TicketSet> prize;
}

