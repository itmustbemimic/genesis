package io.neond.genesis.controller;

import io.neond.genesis.domain.entity.MemberGameResult;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "가입 승인 후에 쓸 친구들", description = "뭐 있지")
public class MemberController {

    private final MemberService memberService;

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
        // TODO ticket_id 출력 안되게
        return memberService.findMemberByAccessToken(request).getTicket();
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

    @Operation(summary = "프로필 이미지 여러장 한번에 받아오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 다운로드 성공"),
            @ApiResponse(responseCode = "500", description = "서버 에러. 로그 확인하기 싫어!")
    })
    @GetMapping("/images")
    public ResponseEntity<List<byte[]>> getImages(@RequestParam List<String> members) throws IOException {
        return memberService.getImages(members);
    }

    @Operation(summary = "프로필 이미지 한장 받아오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이미지 다운로드 성공"),
            @ApiResponse(responseCode = "404", description = "등록된 유저 이미지 없음")
    })
    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String member) throws IOException {
        return memberService.getImage(member);
    }

    @Operation(summary = "qr코드에 쓸 jwt 토큰 받아오기")
    @ApiResponse(responseCode = "200", description = "토큰 발급")
    @GetMapping("/getqrtoken")
    public ResponseEntity getQrToken(HttpServletRequest request) {
        return memberService.getQrToken(
                memberService.findMemberByAccessToken(request)
        );
    }


    @Operation(summary = "마이페이지 - 내 게임 기록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "최신순으로 주르륵"),
            @ApiResponse(responseCode = "500", description = "DB에러 로그 확인 필요")
    })
    @GetMapping("/mygames")
    public List<MemberGameResult> getMemberGameResult(HttpServletRequest request) {
        return memberService.getMemberGameResult(
                memberService.findMemberByAccessToken(request)
        );
    }


    @GetMapping("/ranking/weekly")
    public List<?> getWeeklyRank(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return memberService.getWeeklyRank(date);
    }



}
