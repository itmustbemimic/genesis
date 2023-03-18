package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.addTicketRequestDto;
import io.neond.genesis.domain.entity.Ticket;


public interface TicketService {
    Ticket addTickets(addTicketRequestDto requestDto);
}
