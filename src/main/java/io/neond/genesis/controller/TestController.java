package io.neond.genesis.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.neond.genesis.domain.dto.request.MultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.*;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import io.neond.genesis.service.AdminService;
import io.neond.genesis.service.RoleService;
import io.neond.genesis.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final FirebaseMessaging firebaseMessaging;
    private final TicketHistoryRepository ticketHistoryRepository;

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
        return ticketService.useSingleTickets(requestDto);
    }

    @PutMapping("/addtickets")
    public MyTicketResponseDto addTickets(@RequestBody MultipleTicketRequestDto requestDto) {
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

    @PostMapping("/fcmtest")
    public ResponseEntity fcmTest(@RequestBody FcmTestDto fcmTestDto) {
        Notification notification = Notification.builder()
                .setTitle(fcmTestDto.title)
                .setBody(fcmTestDto.body)
                .setImage(fcmTestDto.image)
                .build();

        Message message = Message.builder()
                .setToken(fcmTestDto.fcmToken)
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(null);
    }

    @Getter
    public class FcmTestDto {
        private String title;
        private String body;
        private String image;
        private String fcmToken;
    }

    @GetMapping("/issued")
    public List<TicketSet> issued() {
        return ticketHistoryRepository.issuedUserBuy();
    }

}
