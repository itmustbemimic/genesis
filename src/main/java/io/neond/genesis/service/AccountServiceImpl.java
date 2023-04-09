package io.neond.genesis.service;

import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final TicketHistoryRepository ticketHistoryRepository;
    @Override
    public IssuedResponseDto issued() {



        return IssuedResponseDto.builder()
               .userBuy(ticketHistoryRepository.IssuedUserBuy())
               .prize(ticketHistoryRepository.IssuedPrize())
               .build();
    }
}
