package io.neond.genesis.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class MemberGameResult {
    @Id
    private String id;
    private String nickname;
    private String gameDate;
    private String gameId;
    private int place;
    private int point;
    private String prizeType;
    private String prizeAmount;

}