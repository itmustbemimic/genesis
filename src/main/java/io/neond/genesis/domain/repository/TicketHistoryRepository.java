package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    List<TicketHistoryResponseDto> findByUuidOrderByDateDesc(String uuid);

    List<TicketHistoryResponseDto> findByUuidAndAmountGreaterThanOrderByDateDesc(String uuid, int amount);
    List<TicketHistoryResponseDto> findByUuidAndAmountLessThanOrderByDateDesc(String uuid, int amount);
}
