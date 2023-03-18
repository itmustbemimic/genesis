package io.neond.genesis.domain.dto.request;

import lombok.Data;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String memberId;
    private String password;
}
