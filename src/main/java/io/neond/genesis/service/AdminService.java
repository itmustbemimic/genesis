package io.neond.genesis.service;

import io.neond.genesis.domain.dto.response.AdminMemberDto;
import io.neond.genesis.domain.dto.response.WaitingMemberDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AdminService {
    ResponseEntity verifyQrToken(String qrToken);
    List<WaitingMemberDto> getWaitingMember();
    List<WaitingMemberDto> searchWaitingMember(String nickname);
    List<AdminMemberDto> getAdminMember();

}
