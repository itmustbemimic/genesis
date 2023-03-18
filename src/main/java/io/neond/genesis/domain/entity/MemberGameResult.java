package io.neond.genesis.domain.entity;

import lombok.Getter;

@Getter
public class MemberGameResult {
    private Member member;
    private String game_date;
    private String game_id;
    private int place;
    private int point;
    private String prize_type;
    private String prize_amount;
}