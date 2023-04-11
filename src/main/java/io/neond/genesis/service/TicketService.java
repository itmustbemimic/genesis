package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.AddMultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.entity.Member;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface TicketService {
    MyTicketResponseDto addMultipleTickets(AddMultipleTicketRequestDto requestDto);
    MyTicketResponseDto addSingleTickets(SingleTicketRequestDto requestDto);
    ResponseEntity useSingleTickets(SingleTicketRequestDto requestDto);
    List<TicketHistoryResponseDto> getMyTicketHistory(Member member);
    List<TicketHistoryResponseDto> getMyUseTicketHistory(Member member);
    List<TicketHistoryResponseDto> getMyAddTicketHistory(Member member);
}
