package io.neond.genesis.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "user_game_history")
public class MemberGameResult {
    @Id
    private String id;
    private String userUuid;
    private String gameDate;
    private String gameId;
    private int place;
    private int point;
    private String prizeType;
    private int prizeAmount;

}