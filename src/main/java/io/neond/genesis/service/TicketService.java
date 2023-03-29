package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.AddMultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import org.springframework.http.ResponseEntity;


public interface TicketService {
    MyTicketResponseDto addMultipleTickets(AddMultipleTicketRequestDto requestDto);
    MyTicketResponseDto addSingleTickets(SingleTicketRequestDto requestDto);
    ResponseEntity useTickets(SingleTicketRequestDto requestDto);
}
