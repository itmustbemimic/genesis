package io.neond.genesis.service;

import io.neond.genesis.domain.dto.MemberCreateDto;
import io.neond.genesis.domain.dto.MyGamesResponseDto;
import io.neond.genesis.domain.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MemberService {
    String createMember(MemberCreateDto createDto);
    boolean idCheck(String memberId);
    boolean nicknameCheck(String nickname);
    void updateRefreshToken(String meberId, String refreshToken);
    Map<String, String> refresh(String refreshToken);
    void updateNickname(Member member, String newNickname);
    Member findMemberByAccessToken(HttpServletRequest request);
    String uploadImage(Member member, MultipartFile file) throws IOException;
    ResponseEntity<List<byte[]>> getImages(List<String> memberList) throws IOException;
    ResponseEntity<byte[]> getImage(String memberId) throws IOException;
    ResponseEntity getQrToken(Member member);
    List<MyGamesResponseDto> getMemberGameResult(Member member);

}
