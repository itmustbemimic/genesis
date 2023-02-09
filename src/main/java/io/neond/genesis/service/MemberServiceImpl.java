package io.neond.genesis.service;

import io.neond.genesis.domain.member.Member;
import io.neond.genesis.domain.member.MemberCreateDto;
import io.neond.genesis.domain.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 아이디"));

        List<SimpleGrantedAuthority> authorities = member.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();

        return new User(member.getMemberId(), member.getPassword(), authorities);
    }

    @Override
    public Long createMember(MemberCreateDto createDto) {
        if (memberRepository.existsByMemberId(createDto.getMemberId())) {
            throw new RuntimeException("이미 존재하는 아이디");
        }

        createDto.encodePassword(passwordEncoder.encode(createDto.getPassword()));
        return memberRepository.save(createDto.toEntity()).getId();
    }
}
