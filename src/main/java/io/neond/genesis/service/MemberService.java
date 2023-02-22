package io.neond.genesis.service;

import io.neond.genesis.domain.dto.MemberCreateDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface MemberService {
    String createMember(MemberCreateDto createDto);
    boolean idCheck(String memberId);
    boolean nicknameCheck(String nickname);
    void updateRefreshToken(String meberId, String refreshToken);
    Map<String, String> refresh(String refreshToken);

    void updateNickname(String accessToken, String newNickname);


}
