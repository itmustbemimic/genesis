package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.MultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto2;
import io.neond.genesis.domain.entity.Member;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface TicketService {
    MyTicketResponseDto addMultipleTickets(MultipleTicketRequestDto requestDto);
    ResponseEntity useMultipleTickets(MultipleTicketRequestDto requestDto);
    MyTicketResponseDto addSingleTickets(SingleTicketRequestDto requestDto, String flag);
    ResponseEntity useSingleTickets(SingleTicketRequestDto requestDto, String flag);
    List<TicketHistoryResponseDto2> getMyTicketHistory(Member member);
    List<TicketHistoryResponseDto2> getMyTicketHistoryByDate(Member member, String s_date, String e_date);
    List<TicketHistoryResponseDto2> getMyTicketHistoryByType(Member member, String type);
    List<TicketHistoryResponseDto2> getMyTicketHistoryByDateAndType(Member member, String s_date, String e_date, String type);

    List<TicketHistoryResponseDto2> getMyUseTicketHistory(Member member);
    List<TicketHistoryResponseDto2> getMyAddTicketHistory(Member member);
}
