package io.neond.genesis.service;

import io.neond.genesis.domain.member.Member;
import io.neond.genesis.domain.member.MemberRepository;
import io.neond.genesis.domain.role.Role;
import io.neond.genesis.domain.role.RoleRepository;
import io.neond.genesis.domain.role.RoleToMemberRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long saveRole(String roleName) {
        if (roleRepository.existsByName(roleName)) {
            throw new RuntimeException("이미 존재하는 role");
        }
        return roleRepository.save(new Role(roleName)).getId();
    }

    @Override
    public Long permitMember(RoleToMemberRequestDto requestDto) {
        Member member = memberRepository.findByMemberId(requestDto.getMemberId()).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디 "));
        Role role = roleRepository.findByName(requestDto.getRoleName()).orElseThrow(() -> new RuntimeException("찾을 수 없는 role"));
        member.getRoles().add(role);

        return member.getId();
    }


}
