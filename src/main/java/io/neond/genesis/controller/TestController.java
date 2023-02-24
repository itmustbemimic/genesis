package io.neond.genesis.controller;

import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.service.MemberService;
import io.neond.genesis.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
@Tag(name = "테스트 api")
public class TestController {

    private final RoleService roleService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/addrole")
    public ResponseEntity<Long> saveRole(@RequestBody String roleName) {
        return ResponseEntity.ok(roleService.saveRole(roleName));
    }

    @GetMapping("/findall")
    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    



}
