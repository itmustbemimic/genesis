package io.neond.genesis.domain.dto.request;

import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.TicketHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultipleTicketRequestDto {
    private String uuid;
    private int blackAmount;
    private int redAmount;
    private int goldAmount;
    private String summary;
    private String flag;

    public TicketHistory toEntity(String _uuid, String _type, int _amount) {
        return TicketHistory.builder()
                .uuid(_uuid)
                .type(_type)
                .summary(summary)
                .amount(_amount)
                .date(Instant.now().toString())
                .flag(flag)
                .build();
    }

    public MultipleTicketRequestDto byAdmin(Member member) {
        return MultipleTicketRequestDto.builder()
                .uuid(uuid)
                .blackAmount(blackAmount)
                .redAmount(redAmount)
                .goldAmount(goldAmount)
                .summary("Admin: " + member.getMemberId())
                .flag("charge")
                .build();
    }

    public MultipleTicketRequestDto useToAdd() {
        return MultipleTicketRequestDto.builder()
                .uuid(uuid)
                .blackAmount(blackAmount * -1)
                .redAmount(redAmount * -1)
                .goldAmount(goldAmount* -1)
                .summary(summary)
                .flag(flag)
                .build();
    }
}
