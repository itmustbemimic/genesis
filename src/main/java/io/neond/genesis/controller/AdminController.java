package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.RoleToMemberRequestDto;
import io.neond.genesis.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name = "관리자용 api", description = "가입 승인, 게임 만들기 뭐 이런거")
public class AdminController {
    private final RoleService roleService;

    @GetMapping("/admin")
    public String checkAdmin() {
        return "어드민 ㅎㅇ";
    }

    @PostMapping("/permit")
    public ResponseEntity<Long> permitMember(@RequestBody RoleToMemberRequestDto requestDto) {
        return ResponseEntity.ok(roleService.permitMember(requestDto));
    }


}