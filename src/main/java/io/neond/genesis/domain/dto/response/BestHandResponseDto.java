package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BestHandResponseDto {
    private String card1;
    private String card2;
    private String card3;
    private String card4;
    private String card5;
    private String nickname;
}
