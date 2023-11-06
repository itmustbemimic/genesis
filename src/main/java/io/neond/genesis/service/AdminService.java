package io.neond.genesis.service;

import io.neond.genesis.domain.dto.response.FullMemberDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto2;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AdminService {
    ResponseEntity verifyQrToken(String qrToken);
    List<FullMemberDto> getWaitingMember();
    List<FullMemberDto> searchWaitingMember(String nickname);
    List<FullMemberDto> getAdminMember();
    List<FullMemberDto> searchAdminMember(String nickname);
    List<FullMemberDto> getPermittedMember();
    List<FullMemberDto> searchPermittedMember(String nickname);
    List<TicketHistoryResponseDto2> getUserChargeHistory(String uuid);
    List<TicketHistoryResponseDto2> getUserUseHistory(String uuid);
    MyTicketResponseDto getUserTickets(String uuid);
}
