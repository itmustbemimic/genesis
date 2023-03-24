package io.neond.genesis.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddTicketRequestDto {
    private String uuid;
    private int blackAmount;
    private int redAmount;
    private int goldAmount;
}
