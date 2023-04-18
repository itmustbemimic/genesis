package io.neond.genesis.domain.repository;

import io.neond.genesis.domain.entity.Bingo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BingoRepository extends JpaRepository<Bingo, Long> {
    Bingo findByUserUuid(String uuid);
}
