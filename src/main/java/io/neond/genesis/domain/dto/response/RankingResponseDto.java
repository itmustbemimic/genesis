package io.neond.genesis.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public interface RankingResponseDto {
    @Schema(example = "1")
    int getRanking();

    @Schema(example = "유저닉네임")
    String getNickname();

    @Schema(example = "47")
    int getPoints();
}