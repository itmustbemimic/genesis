package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.request.AddMultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.AdminMemberDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.dto.response.SearchNicknameDto;
import io.neond.genesis.domain.dto.response.WaitingMemberDto;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.service.AdminService;
import io.neond.genesis.service.RoleService;
import io.neond.genesis.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/test")
@Tag(name = "테스트 api")
public class TestController {

    private final RoleService roleService;
    private final MemberRepository memberRepository;
    private final AdminService adminService;
    private final TicketService ticketService;

    @PostMapping("/addrole")
    public ResponseEntity<Long> saveRole(@RequestBody String roleName) {
        return ResponseEntity.ok(roleService.saveRole(roleName));
    }

    @GetMapping("/findall")
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @GetMapping("/waiting")
    public List<WaitingMemberDto> getWaitingMembers(@RequestParam @Nullable String nickname){
        return nickname == null ? adminService.getWaitingMember() : adminService.searchWaitingMember(nickname);
    }

    @PutMapping("/usetickets")
    public ResponseEntity useTickets(@RequestBody SingleTicketRequestDto requestDto) {
        return ticketService.useTickets(requestDto);
    }

    @PutMapping("/addtickets")
    public MyTicketResponseDto addTickets(@RequestBody AddMultipleTicketRequestDto requestDto) {
        return ticketService.addMultipleTickets(requestDto);
    }

    @PutMapping("/addsingle")
    public MyTicketResponseDto addSingle(@RequestBody SingleTicketRequestDto requestDto) {
        return ticketService.addSingleTickets(requestDto);
    }

    @GetMapping("/admins")
    public List<AdminMemberDto> getAdmins() {
        return adminService.getAdminMember();
    }

}
