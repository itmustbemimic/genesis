package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.dto.response.ConsumptionResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.dto.response.TicketSet;
import io.neond.genesis.domain.entity.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
    List<TicketHistoryResponseDto> findByUuidOrderByDateDesc(String uuid);
    List<TicketHistoryResponseDto> findByUuidAndAmountGreaterThanOrderByDateDesc(String uuid, int amount);
    List<TicketHistoryResponseDto> findByUuidAndAmountLessThanOrderByDateDesc(String uuid, int amount);
    List<TicketHistoryResponseDto> findByUuidAndFlagOrderByDateDesc(String uuid, String flag);
    List<TicketHistoryResponseDto> findByAmountGreaterThanAndDateContainsAndFlagNotContains(int amount, String date, String summary);
    List<TicketHistoryResponseDto> findByAmountLessThanAndDateBetweenAndFlagNotContainsOrderByDateAsc(int amount, String startDate, String endDate, String summary);
    List<TicketHistoryResponseDto> findByDateBetweenAndFlagNotContainsOrderByDateAsc(String startDate, String endDate, String flag);

    @Query(value =
            "SELECT type, SUM(amount) as amount " +
            "FROM ticket_history " +
            "WHERE amount > 0 AND flag=\"charge\"" +
            "GROUP BY type",
            nativeQuery = true)
    List<TicketSet> issuedUserBuy();

    @Query(value =
            "SELECT type, SUM(amount) as amount " +
            "FROM ticket_history " +
            "WHERE amount > 0 AND flag=\"game\"" +
            "GROUP BY type",
            nativeQuery = true)
    List<TicketSet> issuedPrize();

    @Query(value =
            "SELECT type, SUM(amount) as amount " +
            "FROM ticket_history " +
            "WHERE amount > 0 AND date BETWEEN :startDate AND :endDate AND flag IN ('charge', 'game') " +
            "GROUP BY type "
            ,nativeQuery = true)
    List<TicketSet> issuedBetween(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value =
            "SELECT SUM(amount) as amount, MONTH(date) as month " +
            "FROM ticket_history " +
            "WHERE amount < 0 AND flag NOT IN ('gift') " +
            "GROUP BY month " +
            "LIMIT 12",
            nativeQuery = true)
    List<ConsumptionResponseDto> consumptionMonthly();

    @Query(value =
            "SELECT type, SUM(amount) as amount " +
            "FROM ticket_history " +
            "WHERE amount < 0 AND date BETWEEN :startDate AND :endDate AND flag NOT IN ('gift') " +
            "GROUP BY type "
            ,nativeQuery = true)
    List<TicketSet> consumptionDetails(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value =
            "SELECT type, SUM(amount) as amount " +
            "FROM ticket_history " +
            "GROUP BY type"
            ,nativeQuery = true)
    List<TicketSet> circulation();
}
