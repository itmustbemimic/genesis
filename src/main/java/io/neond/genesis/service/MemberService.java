package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.MemberCreateDto;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.MemberGameResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberService {
    String createMember(MemberCreateDto createDto);
    boolean idCheck(String memberId);
    boolean nicknameCheck(String nickname);
    void updateRefreshToken(String meberId, String refreshToken);
    Map<String, String> refresh(String refreshToken);
    void updateNickname(Member member, String newNickname);
    Member findMemberByAccessToken(HttpServletRequest request);
    String uploadImage(Member member, MultipartFile file) throws IOException;
    ResponseEntity<byte[]> getImage(String memberId) throws IOException;
    ResponseEntity getQrToken(Member member);
    List<MemberGameResult> getMemberGameResult(Member member);
    List<?> getWeeklyRank(Date weekStart);
    List<?> getMonthlyRank(Date weekStart);

}
