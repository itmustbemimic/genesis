package io.neond.genesis.service;

import io.neond.genesis.domain.dto.MemberCreateDto;
import io.neond.genesis.domain.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface MemberService {
    String createMember(MemberCreateDto createDto);
    boolean idCheck(String memberId);
    boolean nicknameCheck(String nickname);
    void updateRefreshToken(String meberId, String refreshToken);
    Map<String, String> refresh(String refreshToken);
    void updateNickname(String accessToken, String newNickname);
    Member findByAccessToken(String accessToken);
    String uploadImage(String accessToken, MultipartFile file) throws IOException;

    ResponseEntity<byte[]> getImage(String accessToken) throws IOException;


}
