package io.neond.genesis.domain.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String memberId;
    private String password;
}
