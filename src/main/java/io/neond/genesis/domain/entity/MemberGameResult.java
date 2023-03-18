package io.neond.genesis.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class MemberGameResult {
    private String user_uuid;
    private String game_date;
    private String game_id;
    private int place;
    private int point;
    private String prize_type;
    private String prize_amount;

    @Id
    private Long id;
}