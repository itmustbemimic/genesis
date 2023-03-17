package io.neond.genesis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class rankingResponseDto {
    private int rank;
    private String nickname;
    private int points;
}
