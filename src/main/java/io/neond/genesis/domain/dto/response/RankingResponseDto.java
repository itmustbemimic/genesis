package io.neond.genesis.domain.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class RankingResponseDto {
    private int ranking;
    private String user_uuid;
    private int points;
}
