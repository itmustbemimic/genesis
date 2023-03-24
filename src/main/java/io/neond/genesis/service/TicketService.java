package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.AddTicketRequestDto;
import io.neond.genesis.domain.dto.request.UseTicketRequestDto;
import io.neond.genesis.domain.dto.response.ErrorResponse;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.entity.TicketHistory;
import org.springframework.http.ResponseEntity;


public interface TicketService {
    Ticket addTickets(AddTicketRequestDto requestDto);
    ResponseEntity useTickets(UseTicketRequestDto requestDto);
}
