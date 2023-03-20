package io.neond.genesis.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public interface RankingResponseDto {
    @Schema(example = "1")
    int getRanking();

    @Schema(example = "유저닉네임")
    String getNickname();

    @Schema(example = "47")
    int getPoints();

    @Schema(example = "7a034705-7207-4791-9d0a-1e8371008b4b")
    String getUuid();
}