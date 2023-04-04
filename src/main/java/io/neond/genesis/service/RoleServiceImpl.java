package io.neond.genesis.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.entity.Role;
import io.neond.genesis.domain.repository.RoleRepository;
import io.neond.genesis.domain.dto.request.RoleToMemberRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public Long saveRole(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new RuntimeException("이미 존재하는 role");
        }
        return roleRepository.save(new Role(roleName)).getId();
    }

    @Override
    public String permitMember(RoleToMemberRequestDto requestDto) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디 "));
        Role role = roleRepository.findByName("ROLE_PERMITTED").orElseThrow(() -> new RuntimeException("찾을 수 없는 role"));
        member.getRoles().add(role);

        Notification notification = Notification.builder()
                .setTitle("title")
                .setBody("body")
                .setImage("image")
                .build();

        Message message = Message.builder()
                .setToken("member.fcmtoken")
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }

        return member.getMemberId();
    }


}
