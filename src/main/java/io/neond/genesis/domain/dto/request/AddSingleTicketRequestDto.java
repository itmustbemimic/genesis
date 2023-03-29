package io.neond.genesis.domain.dto.request;

import io.neond.genesis.domain.entity.TicketHistory;
import lombok.Getter;

import java.time.Instant;

@Getter
public class AddSingleTicketRequestDto {
    private String uuid;
    private String type;
    private String usage;
    private int amount;

    public TicketHistory toEntity() {
        return TicketHistory.builder()
                .uuid(uuid)
                .type(type)
                .summary(usage)
                .amount(amount)
                .date(Instant.now().toString())
                .build();
    }
}
