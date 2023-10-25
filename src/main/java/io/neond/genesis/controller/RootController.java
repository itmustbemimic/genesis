package io.neond.genesis.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.neond.genesis.domain.dto.request.MemberCreateDto;
import io.neond.genesis.domain.dto.request.SmsMessageDto;
import io.neond.genesis.service.MemberService;
import io.neond.genesis.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static io.neond.genesis.security.Constants.*;
import static io.neond.genesis.security.Constants.RT_HEADER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "로그인 전에 쓸 친구들", description = "회원가입 로그인 뭐 그런거")
public class RootController {

    private final MemberService memberService;
    private final SmsService smsService;

    @Operation(summary = "회원가입")
    @ApiResponse(responseCode = "201", description = "닉네임 리턴")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberCreateDto createDto) {
        return ResponseEntity.created(null).body(memberService.createMember(createDto));
    }

    @Operation(summary = "아이디 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 아님. 사용 가능", content = @Content),
            @ApiResponse(responseCode = "409", description = "중복. 사용 불가", content = @Content)
    })
    @GetMapping("/idcheck")
    public HttpStatus idCheck(@RequestParam String memberId) {
        if (memberService.idCheck(memberId)) {
            return CONFLICT;
        } else {
            return OK;
        }
    }

    @Operation(summary = "인증 번호 요청. authKey 안보내도 됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전송 성공", content = @Content),
            @ApiResponse(responseCode = "500", description = "전송 실패. 다시 한번 시도", content = @Content)
    })
    @PostMapping("/requestVerify")
    public HttpStatus requestVerify(@RequestBody SmsMessageDto messageDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, URISyntaxException, InvalidKeyException, JsonProcessingException {
        smsService.sendSms(messageDto);
        return CREATED;
    }

    @Operation(summary = "인증번호 확인. 전화번호, 인증번호 둘다 보내야 됨")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "409", description = "인증 실패")
    })
    @PostMapping("/verify")
    public HttpStatus verify(@RequestBody SmsMessageDto messageDto) {
        return smsService.verifySms(messageDto);
    }

    @Operation(summary = "닉네임 중복 체크")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "중복 아님. 사용 가능"),
            @ApiResponse(responseCode = "409", description = "중복. 사용 불가")
    })
    @GetMapping("/nicknamecheck")
    public ResponseEntity<Void> nicknameCheck(@RequestParam String nickname) {
        if (memberService.nicknameCheck(nickname)) {
            return new ResponseEntity<>(CONFLICT);
        } else {
            return new ResponseEntity<>(OK);
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
