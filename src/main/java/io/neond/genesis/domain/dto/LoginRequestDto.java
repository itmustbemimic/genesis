package io.neond.genesis.domain.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String memberId;
    private String password;
}
