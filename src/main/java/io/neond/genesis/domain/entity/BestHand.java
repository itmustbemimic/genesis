package io.neond.genesis.domain.entity;

import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.domain.repository.MemberRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestHand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String card1;
    private String card2;
    private String card3;
    private String card4;
    private String card5;
    private String userUuid;
    private String date;

    public BestHandResponseDto toResponse(String nickname) {
        return BestHandResponseDto.builder()
                .card1(card1)
                .card2(card2)
                .card3(card3)
                .card4(card4)
                .card5(card5)
                .nickname(nickname)
                .build();
    }
}
