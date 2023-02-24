package io.neond.genesis.controller;

import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.repository.TicketRepository;
import io.neond.genesis.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.neond.genesis.security.Constants.TOKEN_HEADER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "가입 승인 후에 쓸 친구들", description = "뭐 있지")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final TicketRepository ticketRepository;

    @Operation(summary = "가입 승인된 유저 권한 테스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "승인된 유저 맞음"),
            @ApiResponse(responseCode = "403", description = "승인된 유저 아님")
    })
    @GetMapping("/test")
    public String checkUser() {
        return "유저 ㅎㅇ";
    }

    @Operation(summary = "가진 티켓 조회", description = "jwt에 있는 subject로 티켓 수량 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블랙 레드 골드 티켓 수량 리턴")
    })
    @GetMapping("/tickets")
    public Ticket getMyTickets(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String accessToken = authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());

        // TODO ticket_id 출력 안되게
        return memberService.findByAccessToken(accessToken).getTicket();
    }

    @Operation(summary = "닉네임 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "닉네임 변경 성공"),
            @ApiResponse(responseCode = "409", description = "닉네임 중복. 변경 실패")
    })
    @PutMapping("/changenick")
    public ResponseEntity changeNickname(@RequestBody String nickname, HttpServletRequest request) {
        if (memberService.nicknameCheck(nickname)) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            String accessToken = authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
            memberService.updateNickname(accessToken, nickname);

            return ResponseEntity.created(null).body("뭐 돌려주지");
        }
    }


}
