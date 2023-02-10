package io.neond.genesis.controller;

import io.neond.genesis.domain.member.Member;
import io.neond.genesis.domain.member.MemberRepository;
import io.neond.genesis.service.MemberService;
import io.neond.genesis.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    private final RoleService roleService;
    private final MemberRepository memberRepository;

    @PostMapping("/addrole")
    public ResponseEntity<Long> saveRole(@RequestBody String roleName) {
        return ResponseEntity.ok(roleService.saveRole(roleName));
    }

    @GetMapping("/all")
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
