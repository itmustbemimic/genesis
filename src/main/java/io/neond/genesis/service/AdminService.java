package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.BestHandRequestDto;
import io.neond.genesis.domain.dto.response.AdminMemberDto;
import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.domain.dto.response.WaitingMemberDto;
import io.neond.genesis.domain.entity.BestHand;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AdminService {
    ResponseEntity verifyQrToken(String qrToken);
    List<WaitingMemberDto> getWaitingMember();
    List<WaitingMemberDto> searchWaitingMember(String nickname);
    List<AdminMemberDto> getAdminMember();
}
