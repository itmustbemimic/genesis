package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;


public interface MyGamesDto {
    String getNickname();
    String getGame_date();
    String getGame_id();
    int getPlace();
    int getPoint();
    String getPrize_type();
    String getPrize_amount();
}
