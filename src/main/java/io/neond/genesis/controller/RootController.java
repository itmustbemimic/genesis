package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.MemberCreateDto;
import io.neond.genesis.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.neond.genesis.security.Constants.*;
import static io.neond.genesis.security.Constants.RT_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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

    @Operation(summary = "리프레시 토큰으로 액세스 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "액세스 토큰 리턴"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 리프레시 토큰"),
            @ApiResponse(responseCode = "401", description = "리프레시 토큰 만료")
    })
    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(TOKEN_HEADER_PREFIX)) {
            throw new RuntimeException("TestController: 토큰 존재 x");
        }

        String refreshToken = authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());
        Map<String, String> tokens = memberService.refresh(refreshToken);
        response.setHeader(AT_HEADER, tokens.get(AT_HEADER));

        if (tokens.get(RT_HEADER) != null) {
            response.setHeader(RT_HEADER, tokens.get(RT_HEADER));
        }

        return ResponseEntity.ok(tokens);
    }
}
