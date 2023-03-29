package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.AddMultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.UseTicketRequestDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.entity.Ticket;
import org.springframework.http.ResponseEntity;


public interface TicketService {
    MyTicketResponseDto addMultipleTickets(AddMultipleTicketRequestDto requestDto);
    ResponseEntity useTickets(UseTicketRequestDto requestDto);
}
