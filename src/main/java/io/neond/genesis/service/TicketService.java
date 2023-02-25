package io.neond.genesis.service;

import io.neond.genesis.domain.dto.addTicketRequestDto;
import io.neond.genesis.domain.entity.Ticket;


public interface TicketService {
    Ticket addTickets(addTicketRequestDto requestDto);
}
