package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.service.GameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
@Tag(name = "미니게임 api", description = "베스트핸드, 빙고")
public class GameController {

    private final GameService gameService;

    @GetMapping
    public BestHandResponseDto getBestHand() {
        return gameService.getBestHand();
    }
}
