package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyMonthlyGameDto {
    private String gameDate;
    private String gameId;
    private int point;
    private String prizeType;
    private int prizeAmount;
    private int place;
}
