package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    List<TicketHistoryResponseDto> findByUuidOrderByDateDesc(String uuid);
    List<TicketHistoryResponseDto> findByUuidAndAmountGreaterThanOrderByDateDesc(String uuid, int amount);
    List<TicketHistoryResponseDto> findByUuidAndAmountLessThanOrderByDateDesc(String uuid, int amount);


    @Query(value =
            "SELECT SUM(amount) as amount, MONTH(date) as month " +
            "FROM ticket_history " +
            "WHERE amount > 0 AND summary not like \"%선물%\" " +
            "GROUP BY month " +
            "LIMIT 5",
            nativeQuery = true)
    List<IssuedResponseDto> issuedMonthly();
}
