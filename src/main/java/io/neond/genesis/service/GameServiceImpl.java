package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.BestHandRequestDto;
import io.neond.genesis.domain.dto.request.BingoRequestDto;
import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.domain.dto.response.BingoResponseDto;
import io.neond.genesis.domain.entity.Bingo;
import io.neond.genesis.domain.repository.BestHandRepository;
import io.neond.genesis.domain.repository.BingoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService{

    private final BestHandRepository bestHandRepository;
    private final BingoRepository bingoRepository;

    @Override
    public BestHandResponseDto getBestHand() {
        return bestHandRepository.findTop1ByOrderByDateDesc();
    }

    @Override
    public BestHandResponseDto setBestHand(BestHandRequestDto requestDto) {
        return bestHandRepository.save(requestDto.toEntity()).toResponse();
    }

    @Override
    public Bingo getBingo(String uuid) {
        return bingoRepository.findByUserUuid(uuid);
    }

    @Override
    public Bingo setBingo(Bingo requestDto) {
        Bingo bingo = bingoRepository.findByUserUuid(requestDto.getUserUuid());
        if (bingo != null) {
            requestDto.setId(bingo.getId());
        }
        return bingoRepository.save(requestDto);
    }
}
