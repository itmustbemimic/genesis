package io.neond.genesis.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

public interface IssuedResponseDto {
    int getMonth();
    int getAmount();
}
