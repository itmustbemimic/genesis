package io.neond.genesis.controller;

import io.neond.genesis.domain.member.MemberCreateDto;
import io.neond.genesis.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Long> join(@RequestBody MemberCreateDto createDto) {
        return ResponseEntity.ok(memberService.createMember(createDto));
    }


}
