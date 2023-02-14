package io.neond.genesis.service;

import io.neond.genesis.domain.dto.addTicketRequestDto;
import io.neond.genesis.domain.entity.Ticket;

import java.util.Optional;

public interface TicketService {
    String addTickets(addTicketRequestDto requestDto);
}
