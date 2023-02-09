package io.neond.genesis.service;

import io.neond.genesis.domain.member.MemberCreateDto;
import io.neond.genesis.domain.token.LoginResponseDto;

import java.util.Map;

public interface MemberService {
    Long createMember(MemberCreateDto createDto);

    void updateRefreshToken(String meberId, String refreshToken);

    Map<String, String> refresh(String refreshToken);

}
