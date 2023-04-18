package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.BestHandRequestDto;
import io.neond.genesis.domain.dto.request.BingoRequestDto;
import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.domain.dto.response.BingoResponseDto;
import io.neond.genesis.domain.entity.Bingo;

public interface GameService {
    BestHandResponseDto getBestHand();
    BestHandResponseDto setBestHand(BestHandRequestDto requestDto);
    Bingo getBingo(String uuid);
    Bingo setBingo(Bingo requestDto);


}
