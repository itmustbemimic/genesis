package io.neond.genesis.domain.dto.request;

import io.neond.genesis.domain.entity.BestHand;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Calendar;

@Getter
@Slf4j
public class BestHandRequestDto {
    private String card1;
    private String card2;
    private String card3;
    private String card4;
    private String card5;
    private String nickname;

    public BestHand toEntity() {
        log.info(Calendar.getInstance().toString());
        return BestHand.builder()
                .card1(card1)
                .card2(card2)
                .card3(card3)
                .card4(card4)
                .card5(card5)
                .nickname(nickname)
                .date(Instant.now().toString())
                .build();
    }
}
