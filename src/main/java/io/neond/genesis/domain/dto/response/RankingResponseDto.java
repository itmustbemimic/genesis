package io.neond.genesis.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RankingResponseDto {
    private int rank;
    private String nickname;
    private int points;
}
