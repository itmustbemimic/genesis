package io.neond.genesis.domain.dto.request;

import io.neond.genesis.domain.entity.TicketHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddMultipleTicketRequestDto {
    private String uuid;
    private int blackAmount;
    private int redAmount;
    private int goldAmount;
    private String summary;

    public TicketHistory toEntity(String _uuid, String _type, int _amount) {
        return TicketHistory.builder()
                .uuid(_uuid)
                .type(_type)
                .summary(summary)
                .amount(_amount)
                .date(Instant.now().toString())
                .build();
    }
}
