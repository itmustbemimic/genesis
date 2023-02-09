package io.neond.genesis.controller;

import io.neond.genesis.domain.role.RoleToMemberRequestDto;
import io.neond.genesis.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final RoleService roleService;

    @PostMapping("/permit")
    public ResponseEntity<Long> permitMember(@RequestBody RoleToMemberRequestDto requestDto) {
        return ResponseEntity.ok(roleService.permitMember(requestDto));
    }
}