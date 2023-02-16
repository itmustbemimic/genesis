package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
