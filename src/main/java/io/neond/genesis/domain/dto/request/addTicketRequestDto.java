package io.neond.genesis.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class addTicketRequestDto {
    private String uuid;
    private int blackAmount;
    private int redAmount;
    private int goldAmount;
}
