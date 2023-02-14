package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.RoleToMemberRequestDto;
import io.neond.genesis.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


}