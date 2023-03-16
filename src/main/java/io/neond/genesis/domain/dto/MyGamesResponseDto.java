package io.neond.genesis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyGamesResponseDto {
    private final String user_id;
    private final String date;
    private final String game_id;
    private final int point;
    private final int place;
    private final String prize_type;
    private final int prize_amount;
}
