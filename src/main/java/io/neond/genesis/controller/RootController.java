package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.MemberCreateDto;
import io.neond.genesis.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "로그인 전에 쓸 친구들", description = "회원가입 로그인 뭐 그런거")
public class RootController {

    private final MemberService memberService;

    //TODO 푸시 구현되면 회원 가입 리스폰스에 푸시용 폰 토큰 전송
    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201", description = "닉네임 리턴")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberCreateDto createDto) {
        return ResponseEntity.created(null).body(memberService.createMember(createDto));
    }

    @Operation(summary = "아이디 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 아님. 사용 가능"),
            @ApiResponse(responseCode = "409", description = "중복. 사용 불가")
    })
    @GetMapping("/idcheck")
    public ResponseEntity idCheck(@RequestParam String memberId) {
        if (memberService.idCheck(memberId)) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
    }

    @Operation(summary = "닉네임 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 아님. 사용 가능"),
            @ApiResponse(responseCode = "409", description = "중복. 사용 불가")
    })
    @GetMapping("/nicknamecheck")
    public ResponseEntity nicknameCheck(@RequestParam String nickname) {
        if (memberService.nicknameCheck(nickname)) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(409));
        } else {
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
    }
}