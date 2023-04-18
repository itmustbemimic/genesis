package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.request.BestHandRequestDto;
import io.neond.genesis.domain.dto.request.RoleToMemberRequestDto;
import io.neond.genesis.domain.dto.request.MultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.BestHandResponseDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.dto.response.FullMemberDto;
import io.neond.genesis.domain.entity.Bingo;
import io.neond.genesis.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "관리자용 api", description = "가입 승인, 게임 만들기 뭐 이런거")
public class AdminController {
    private final RoleService roleService;
    private final TicketService ticketService;
    private final AdminService adminService;
    private final MemberService memberService;
    private final GameService gameService;

    @Operation(summary = "admin 권한 테스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "admin 맞음"),
            @ApiResponse(responseCode = "403", description = "admin 아님")
    })
    @GetMapping("/test")
    public String checkAdmin() {

        return "어드민 ㅎㅇ";
    }

    @Operation(summary = "유저 가입 허가")
    @ApiResponse(responseCode = "201", description = "유저 아이디 리턴")
    @PutMapping("/permit")
    public ResponseEntity<String> permitMember(@RequestBody RoleToMemberRequestDto requestDto) {
        return ResponseEntity.created(null).body(roleService.permitMember(requestDto));
    }

    @Operation(summary = "유저 가입 거부")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 유저 정보 삭제"),
            @ApiResponse(responseCode = "409", description = "해당 유저에게 권한이 있으면 삭제 x")
    })
    @PutMapping("/reject")
    public ResponseEntity rejectMember(@RequestBody RoleToMemberRequestDto requestDto) {
        return roleService.rejectMember(requestDto);
    }

    @Operation(summary = "유저에게 티켓 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "티켓 추가 성공. 해당 티켓 테이블 리턴"),
            @ApiResponse(responseCode = "400", description = "찾을 수 없는 아이디 or 입력값 에러")
    })
    @PutMapping("/addtickets")
    public ResponseEntity<MyTicketResponseDto> addTickets(HttpServletRequest request, @RequestBody MultipleTicketRequestDto requestDto) {
        return ResponseEntity.created(null).body(
                ticketService.addMultipleTickets(requestDto.byAdmin(
                        memberService.findMemberByAccessToken(request)
                )));
    }

    @Operation(summary = "qr코드 토큰 검증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 실명 리턴"),
            @ApiResponse(responseCode = "400", description = "만료 or 유효하지 않은 토큰")
    })
    @GetMapping("/verifyqr")
    public ResponseEntity verifyQr(HttpServletRequest request) {
        return adminService.verifyQrToken(request.getHeader("qrToken"));
    }

    @Operation(summary = "가입 승인 대기 유저들 리스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "파라미터 있으면 가입 승인 대기 유저들 검색"),
            @ApiResponse(responseCode = "500", description = "에러 날 일이 없는데?")
    })
    @GetMapping("/waiting")
    public List<FullMemberDto> getWaitingMembers(@RequestParam @Nullable String nickname){
        return nickname == null ? adminService.getWaitingMember() : adminService.searchWaitingMember(nickname);
    }

    @Operation(summary = "유저 티켓 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공!"),
            @ApiResponse(responseCode = "400", description = "티켓 타입 확인 or 티켓 숫자 0 이하")
    })
    @PutMapping("/usetickets")
    public ResponseEntity useTickets(@RequestBody SingleTicketRequestDto requestDto) {
        return ticketService.useSingleTickets(requestDto);
    }

    @Operation(summary = "어드민 리스트")
    @ApiResponse(responseCode = "200", description = "파라미터 있으면 어드민 닉네임 검색")
    @GetMapping("/list")
    public List<FullMemberDto> getAdminList(@RequestParam @Nullable String nickname) {
        return nickname == null ? adminService.getAdminMember() : adminService.searchAdminMember(nickname);
    }

    @Operation(summary = "허가된 유저 리스트")
    @ApiResponse(responseCode = "200", description = "파라미터 있으면 가입 허가된 유저들 검색")
    @GetMapping("/members")
    public List<FullMemberDto> getPermittedMembers(@RequestParam @Nullable String nickname) {
        return nickname == null ? adminService.getPermittedMember() : adminService.searchPermittedMember(nickname);
    }

    @Operation(summary = "베스트핸드 입력")
    @PostMapping("/game/besthand")
    public BestHandResponseDto setBestHand(@RequestBody BestHandRequestDto requestDto) {
        return gameService.setBestHand(requestDto);
    }

    @PostMapping("/game/bingo")
    public Bingo setBingo(@RequestBody Bingo bingo) {
        return gameService.setBingo(bingo);
    }

}