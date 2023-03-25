package io.neond.genesis.domain.dto.request;

import io.neond.genesis.domain.entity.TicketHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class UseTicketRequestDto {
    private String uuid;
    private String type;
    private String usage;
    private int amount;

    public TicketHistory toEntity(String _uuid){
        return TicketHistory.builder()
                .uuid(_uuid)
                .type(type)
                .summary(usage)
                .amount(amount)
                .date(Instant.now().toString())
                .build();
    }
}
