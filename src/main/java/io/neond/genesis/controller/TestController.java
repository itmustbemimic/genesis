package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.request.AddTicketRequestDto;
import io.neond.genesis.domain.dto.request.UseTicketRequestDto;
import io.neond.genesis.domain.dto.response.SearchNicknameDto;
import io.neond.genesis.domain.dto.response.WaitingMemberDto;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.service.AdminService;
import io.neond.genesis.service.RoleService;
import io.neond.genesis.service.TicketService;
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

    @GetMapping("/search")
    public List<SearchNicknameDto> search(@RequestParam String nickname) {
        return memberRepository.findByNicknameContains(nickname);
    }

    @GetMapping("/waiting")
    public List<WaitingMemberDto> getWaiting() {
        return adminService.getWaitingMember();
    }

    @PutMapping("/usetickets")
    public ResponseEntity useTickets(@RequestBody UseTicketRequestDto requestDto) {
        return ticketService.useTickets(requestDto);
    }

    @PutMapping("/addtickets")
    public Ticket addTickets(@RequestBody AddTicketRequestDto requestDto) {
        return ticketService.addTickets(requestDto);
    }

}
