package io.neond.genesis.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.dto.MemberCreateDto;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.entity.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.validity}")
    private String accessValidity;
    @Value("${jwt.refresh.validity}")
    private String refreshValidity;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 아이디"));

        List<SimpleGrantedAuthority> authorities = member.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();

        return new User(member.getMemberId(), member.getPassword(), authorities);
    }

    @Override
    public String createMember(MemberCreateDto createDto) {
        if (memberRepository.existsByMemberId(createDto.getMemberId())) {
            throw new RuntimeException("이미 존재하는 아이디");
        }

        createDto.encodePassword(passwordEncoder.encode(createDto.getPassword()));
        return memberRepository.save(createDto.toEntity()).getNickname();
    }

    @Override
    public boolean idCheck(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    @Override
    public boolean nicknameCheck(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public void updateRefreshToken(String meberId, String refreshToken) {
        Member member = memberRepository.findByMemberId(meberId).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디 "));
        member.updateRefreshToken(refreshToken);
    }

    @Override
    public Map<String, String> refresh(String refreshToken) {
        // refresh token 유효성 검사
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);

        // access token 재발급
        long now = System.currentTimeMillis();
        String memberId = decodedJWT.getSubject();
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 아이디"));

        if (!member.getRefreshToken().equals(refreshToken)) {
            throw new JWTVerificationException("유효하지 않은 refresh token");
        }

        String accessToken = JWT.create()
                .withSubject(member.getMemberId())
                .withExpiresAt(new Date(now + accessValidity))
                .withClaim("roles", member.getRoles().stream().map(Role::getName)
                        .collect(Collectors.toList()))
                .sign(Algorithm.HMAC256(secretKey));

        Map<String, String> accessTokenResponseMap = new HashMap<>();

        accessTokenResponseMap.put("access_token", accessToken);
        return accessTokenResponseMap;
    }

}
