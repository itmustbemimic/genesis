package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.request.GiveTicketsRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.*;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.service.MemberService;
import io.neond.genesis.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "가입 승인 후에 쓸 친구들", description = "뭐 있지")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final TicketService ticketService;

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
    public MyTicketResponseDto getMyTickets(HttpServletRequest request) {
        return memberService.getMyTicket(memberService.findMemberByAccessToken(request));
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
        }
        memberService.updateNickname(
                memberService.findMemberByAccessToken(request),
                nickname);

        return ResponseEntity.created(null).body("뭐 돌려주지");

    }

    @Operation(summary = "프로필 이미지 업로드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이미지 업로드 성공"),
            @ApiResponse(responseCode = "400", description = "전송된 이미지 없음"),
            @ApiResponse(responseCode = "500", description = "서버에러... 로그 확인 필요")
    })
    @PostMapping("/image")
    public ResponseEntity uploadImage(MultipartFile file, HttpServletRequest request) throws IOException {
        if(file.isEmpty()) {
            return ResponseEntity.badRequest().body("이미지 없음");
        }

        return ResponseEntity.created(null).body(
                memberService.uploadImage(
                        memberService.findMemberByAccessToken(request),
                        file));
    }

    @Operation(summary = "프로필 이미지 한장 받아오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 다운로드 성공"),
            @ApiResponse(responseCode = "200", description = "누군가의 요청으로 이미지가 없어도 200")
    })
    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String memberUuid) throws IOException {
        return memberService.getImage(memberUuid);
    }

    @Operation(summary = "qr코드에 쓸 jwt 토큰 받아오기")
    @ApiResponse(responseCode = "200", description = "토큰 발급")
    @GetMapping("/getqrtoken")
    public ResponseEntity getQrToken(HttpServletRequest request) {
        return memberService.getQrToken(
                memberService.findMemberByAccessToken(request)
        );
    }


    @Operation(summary = "내 월간 게임 기록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최신순으로 주르륵"),
            @ApiResponse(responseCode = "500", description = "DB에러 로그 확인 필요")
    })
    @GetMapping("/mygames/monthly")
    public List<MyMonthlyGameDto> getMonthlyMemberGameResult(HttpServletRequest request,
                                                @Parameter(name = "날짜", description = "해당 날짜가 속한 주의 랭킹. 2023-03-08 입력 시 3월 둘째주 랭킹 출력")
                                                @RequestParam
                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                Date date) {
        return memberService.getMonthlyMemberGameResult(
                memberService.findMemberByAccessToken(request),
                date
        );
    }

    @Operation(summary = "내 전체 게임 기록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최신순으로 주르륵"),
            @ApiResponse(responseCode = "500", description = "DB에러 로그 확인 필요")
    })
    @GetMapping("/mygames")
    public List<MyGamesDto> getMemberGameResult(HttpServletRequest request) {
        return memberService.getMemberGameResult(
                memberService.findMemberByAccessToken(request)
        );
    }

    @Operation(summary = "주간 랭킹")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 1위부터 주르륵")
    })
    @GetMapping("/ranking/weekly")
    public List<RankingResponseDto> getWeeklyRank(
            @Parameter(name = "날짜", description = "해당 날짜가 속한 주의 랭킹. 2023-03-08 입력 시 3월 둘째주 랭킹 출력")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date) {
        return memberService.getWeeklyRank(date);
    }

    @Operation(summary = "월간 랭킹")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "랭킹 1위부터 주르륵")
    })
    @GetMapping("/ranking/monthly")
    public List<RankingResponseDto> getMonthlyRank(
            @Parameter(name = "날짜", description = "해당 날짜가 속한 월의 랭킹. 2023-03-08 입력 시 3월 랭킹 출력")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date) {
        return memberService.getMonthlyRank(date);
    }

    @Operation(summary = "유저닉네임 검색")
    @ApiResponse(responseCode = "200", description = "파라미터 없으면 전체 출력, 있으면 검색")
    @GetMapping("/search")
    public List<SearchNicknameDto> search(@RequestParam @Nullable String nickname) {
        return nickname == null ?
                memberService.getAllPermittedMember() :
                memberService.searchPermittedMember(nickname);
    }

    @Operation(summary = "내 티켓 내역 전체 검색")
    @GetMapping("/ticket/history")
    public List<TicketHistoryResponseDto> getMyTicketHitory(HttpServletRequest request){
        return ticketService.getMyTicketHistory(
                memberService.findMemberByAccessToken(request)
        );
    }

    @Operation(summary = "사용 내역 전체 검색")
    @GetMapping("/ticket/history/use")
    public List<TicketHistoryResponseDto> getMyUseTicketHistory(HttpServletRequest request){
        return ticketService.getMyUseTicketHistory(
                memberService.findMemberByAccessToken(request)
        );

    }

    @Operation(summary = "충전 내역 전체 검색")
    @GetMapping("/ticket/history/add")
    public List<TicketHistoryResponseDto> getMyAddTicketHistory(HttpServletRequest request){
        return ticketService.getMyAddTicketHistory(
                memberService.findMemberByAccessToken(request)
        );
    }

    @Operation(summary = "멤버들끼리 티켓 선물")
    @PutMapping("/ticket/gift")
    public ResponseEntity giveTickets(HttpServletRequest request, @RequestBody GiveTicketsRequestDto requestDto) {
        ResponseEntity flag = ticketService.useTickets(
                new SingleTicketRequestDto(
                        memberService.findMemberByAccessToken(request).getUuid(),
                        requestDto.getType(),
                        memberRepository.findByUuid(requestDto.getTo()).get().getNickname() + "에게 선물하기",
                        requestDto.getAmount()
                )
        );

        if (flag.getStatusCode().equals(HttpStatusCode.valueOf(201))) {
            ticketService.addSingleTickets(
                    new SingleTicketRequestDto(
                            requestDto.getTo(),
                            requestDto.getType(),
                            memberService.findMemberByAccessToken(request).getNickname() + "님이 보낸 선물",
                            requestDto.getAmount()
                    )
            );

            return new ResponseEntity(
                    memberService.findMemberByAccessToken(request).getTicket().toResponseDto(),
                    HttpStatusCode.valueOf(201));

        } else return flag;
    }

    @Operation(summary = "소켓 서버에서 게임 참여 할때 쓰는거")
    @PutMapping("/joingame")
    public ResponseEntity joinGame(HttpServletRequest request, @RequestBody SingleTicketRequestDto requestDto) {
        return ticketService.useTickets(
                requestDto.toJoinGame(
                        memberService.findMemberByAccessToken(request)
                ));
    }

}
