package io.neond.genesis.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.neond.genesis.domain.dto.response.*;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.repository.RoleRepository;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.neond.genesis.security.Constants.TOKEN_HEADER_PREFIX;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final TicketHistoryRepository ticketHistoryRepository;

    @Value("${jwt.secret.qr")
    private String qrSecretKey;

    @Override
    public ResponseEntity verifyQrToken(String qrToken) {
        log.info("hi");
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(qrSecretKey)).build();

        try {
            log.info(qrToken.substring(TOKEN_HEADER_PREFIX.length()));
            DecodedJWT decodedJWT = verifier.verify(qrToken.substring(TOKEN_HEADER_PREFIX.length()));
            Optional<Member> member = memberRepository.findByMemberId(decodedJWT.getSubject());

            return new ResponseEntity(member.get().getName(), HttpStatus.OK);

        } catch (TokenExpiredException e) {
            log.error(String.valueOf(e));
            ErrorResponse errorResponse = new ErrorResponse(400, "만료된 qr 코드");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);

        } catch (JWTVerificationException e) {
            log.error(String.valueOf(e));
            ErrorResponse errorResponse = new ErrorResponse(400, "유효하지 않은 qr 코드");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<FullMemberDto> getWaitingMember() {
        return memberRepository.findMembersByRolesNotContains(
                roleRepository.findByName("ROLE_PERMITTED").orElseThrow(() -> new RuntimeException("찾을 수 없는 role"))
        );
    }

    @Override
    public List<FullMemberDto> searchWaitingMember(String nickname) {
        return memberRepository.findMembersByRolesNotContainsAndNicknameContains(
                roleRepository.findByName("ROLE_PERMITTED").orElseThrow(() -> new RuntimeException("찾을 수 없는 role")),
                nickname
        );
    }

    @Override
    public List<FullMemberDto> getAdminMember() {
        return memberRepository.findByRoles(
                roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new RuntimeException("찾을 수 없는 role"))
        );
    }

    @Override
    public List<FullMemberDto> searchAdminMember(String nickname) {
        return memberRepository.findByNicknameContainingAndRoles(
                nickname,
                roleRepository.findByName("ROLE_ADMIN").orElseThrow()
        );
    }

    @Override
    public List<FullMemberDto> getPermittedMember() {
        return memberRepository.findByRoles(
                roleRepository.findByName("ROLE_PERMITTED").orElseThrow()
        );
    }

    @Override
    public List<FullMemberDto> searchPermittedMember(String nickname) {
        return memberRepository.findByNicknameContainingAndRoles(
                nickname ,
                roleRepository.findByName("ROLE_PERMITTED").orElseThrow()
        );
    }

    @Override
    public List<TicketHistoryResponseDto2> getUserChargeHistory(String uuid) {
        return ticketHistoryRepository.findByUuidAndAmountLessThan(uuid, 0);
    }

    @Override
    public List<TicketHistoryResponseDto2> getUserUseHistory(String uuid) {
        //TODO flag game말고 use로바꾸기 (DB작업 필요)
        return ticketHistoryRepository.findByUuidAndAmountGreaterThan(uuid, 0);
    }

    @Override
    public MyTicketResponseDto getUserTickets(String uuid) {
        Member member = memberRepository.findByUuid(uuid).orElseThrow();
        Ticket ticket = member.getTicket();
        return new MyTicketResponseDto(ticket.getBlack(), ticket.getRed(), ticket.getGold());
    }


}
