package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.dto.response.RankingResponseDto;
import io.neond.genesis.domain.entity.MemberGameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberGameResultRepository extends JpaRepository<MemberGameResult, String> {
    @Query(value =
            "SELECT RANK() OVER (ORDER BY SUM(point) DESC) AS ranking, user_uuid, SUM(point) AS points " +
            "FROM user_game_history " +
            "WHERE game_date BETWEEN :startDate AND :endDate " +
            "GROUP BY user_uuid",
    nativeQuery = true)
    List<?> getRank(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
