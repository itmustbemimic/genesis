package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.RoleToMemberRequestDto;
import io.neond.genesis.domain.dto.addTicketRequestDto;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.service.AdminService;
import io.neond.genesis.service.RoleService;
import io.neond.genesis.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "관리자용 api", description = "가입 승인, 게임 만들기 뭐 이런거")
public class AdminController {
    private final RoleService roleService;
    private final TicketService ticketService;
    private final AdminService adminService;

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

    @Operation(summary = "유저에게 티켓 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "티켓 추가 성공. 해당 티켓 테이블 리턴"),
            @ApiResponse(responseCode = "400", description = "찾을 수 없는 아이디 or 입력값 에러")
    })
    @PutMapping("/addtickets")
    public ResponseEntity<Ticket> addBlackTicketToUser(@RequestBody addTicketRequestDto requestDto) {
        return ResponseEntity.created(null).body(ticketService.addTickets(requestDto));
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



}