package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.dto.response.MyGamesDto;
import io.neond.genesis.domain.dto.response.RankingResponseDto;
import io.neond.genesis.domain.entity.MemberGameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberGameResultRepository extends JpaRepository<MemberGameResult, String> {
    @Query(value =
            "SELECT RANK() OVER (ORDER BY SUM(point) DESC) AS ranking, m.nickname AS nickname, SUM(point) AS points " +
            "FROM user_game_history ugh " +
            "RIGHT JOIN member m ON ugh.user_uuid = m.uuid " +
            "WHERE game_date BETWEEN :startDate AND :endDate " +
            "GROUP BY user_uuid",
        nativeQuery = true)
    List<RankingResponseDto> getRank(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value =
    "SELECT m.nickname AS nickname, game_date, game_id, point, prize_amount, prize_type, place " +
            "FROM user_game_history ugh " +
            "JOIN member m ON ugh.user_uuid = m.uuid " +
            "WHERE user_uuid = :uuid " +
            "ORDER BY game_date DESC",
    nativeQuery = true)
    List<MyGamesDto> getMyGames(@Param("uuid") String uuid);
}
