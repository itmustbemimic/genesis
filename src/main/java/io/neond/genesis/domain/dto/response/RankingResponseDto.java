package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingResponseDto {
    private int ranking;
    private String user_uuid;
    private int points;
}
