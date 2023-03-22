package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.response.SearchNicknameDto;
import io.neond.genesis.domain.dto.response.WaitingMemberDto;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.service.AdminService;
import io.neond.genesis.service.MemberService;
import io.neond.genesis.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
@Tag(name = "테스트 api")
public class TestController {

    private final RoleService roleService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AdminService adminService;

    @PostMapping("/addrole")
    public ResponseEntity<Long> saveRole(@RequestBody String roleName) {
        return ResponseEntity.ok(roleService.saveRole(roleName));
    }

    @GetMapping("/findall")
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @GetMapping("/search")
    public List<SearchNicknameDto> search(@RequestParam String nickname) {
        return memberRepository.findByNicknameContains(nickname);
    }

    @GetMapping("/waiting")
    public List<WaitingMemberDto> getWaiting() {
        return adminService.getWaitingMember();
    }

}
