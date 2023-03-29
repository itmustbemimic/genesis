package io.neond.genesis.domain.entity;

import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private int black;
    private int red;
    private int gold;

    public MyTicketResponseDto toResponseDto() {
        return MyTicketResponseDto.builder()
                .red(red)
                .black(black)
                .gold(gold)
                .build();
    }

}
