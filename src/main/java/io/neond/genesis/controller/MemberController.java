package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.MemberCreateDto;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "유저", description = "회원가입 로그인 뭐 그런거...")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "회원가입")
    @ApiResponse(description = "회원 아이디 리턴")
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberCreateDto createDto) {
        return ResponseEntity.ok().body(memberService.createMember(createDto));
    }



}
