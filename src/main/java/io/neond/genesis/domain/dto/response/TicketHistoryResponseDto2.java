package io.neond.genesis.domain.dto.response;

import io.neond.genesis.domain.repository.MemberRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Getter
@Builder
public class TicketHistoryResponseDto2 {
    String type;
    String summary;
    int amount;
    String date;
    String flag;

    public TicketHistoryResponseDto2 renderNickname(String nickname){
        return TicketHistoryResponseDto2.builder()
                .type(type)
                .summary(nickname)
                .amount(amount)
                .date(date)
                .flag(flag)
                .build();
    }
}


