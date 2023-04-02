package io.neond.genesis.domain.dto.request;

import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.TicketHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Builder
public class SingleTicketRequestDto {

    @Schema(example = "7a034705-7207-4791-9d0a-1e8371008b4b")
    private String uuid;

    @Schema(example = "red")
    private String type;

    @Schema(example = "음료수 사먹엇엉")
    private String usage;

    @Schema(example = "2")
    private int amount;

    public TicketHistory toEntity(){
        return TicketHistory.builder()
                .uuid(uuid)
                .type(type)
                .summary(usage)
                .amount(amount)
                .date(Instant.now().toString())
                .build();
    }

    public SingleTicketRequestDto toJoinGame(Member member) {
        return SingleTicketRequestDto.builder()
                .uuid(member.getUuid())
                .type(type)
                .usage(usage)
                .amount(amount)
                .build();
    }
}
