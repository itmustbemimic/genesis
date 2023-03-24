package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
}
