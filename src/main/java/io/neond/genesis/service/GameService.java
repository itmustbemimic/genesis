package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.BestHandRequestDto;
import io.neond.genesis.domain.dto.response.BestHandResponseDto;

public interface GameService {
    BestHandResponseDto getBestHand();
    BestHandResponseDto setBestHand(BestHandRequestDto requestDto);

}
