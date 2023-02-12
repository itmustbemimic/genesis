package io.neond.genesis.service;

import io.neond.genesis.domain.dto.MemberCreateDto;

import java.util.Map;

public interface MemberService {
    String createMember(MemberCreateDto createDto);

    void updateRefreshToken(String meberId, String refreshToken);

    Map<String, String> refresh(String refreshToken);

}
