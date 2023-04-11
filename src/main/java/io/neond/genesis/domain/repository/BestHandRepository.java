package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.domain.entity.BestHand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestHandRepository extends JpaRepository<BestHand, Long> {
    BestHandResponseDto findTop1ByOrderByDateDesc();

}
