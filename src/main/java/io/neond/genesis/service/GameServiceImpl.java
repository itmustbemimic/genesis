package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.BestHandRequestDto;
import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.domain.repository.BestHandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    private final BestHandRepository bestHandRepository;

    @Override
    public BestHandResponseDto getBestHand() {
        return bestHandRepository.findTop1ByOrderByDateDesc();
    }

    @Override
    public BestHandResponseDto setBestHand(BestHandRequestDto requestDto) {
        return bestHandRepository.save(requestDto.toEntity()).toResponse();
    }
}
