package io.neond.genesis.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.dto.MemberCreateDto;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.entity.Role;
import io.neond.genesis.domain.repository.TicketRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

import static io.neond.genesis.security.Constants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService, UserDetailsService {

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.validity}")
    private Long accessValidity;
    @Value("${jwt.refresh.validity}")
    private Long refreshValidity;

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

        // 회원가입할때 티켓 테이블 항목 생성
        Ticket ticket = ticketRepository.save(
                Ticket.builder()
                        .black(0)
                        .red(0)
                        .gold(0)
                        .build()
        );

        createDto.encodePassword(passwordEncoder.encode(createDto.getPassword()));
        return memberRepository.save(createDto.toEntity(ticket)).getNickname();
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
    public void updateRefreshToken(String memberId, String refreshToken) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디 "));
        member.updateRefreshToken(refreshToken);
    }

    @Override //액세스 토큰 재발급
    public Map<String, String> refresh(String refreshToken) {
        // refresh token 유효성 검사
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);

        // access token 재발급
        String memberId = decodedJWT.getSubject();
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 아이디"));

        if (!member.getRefreshToken().equals(refreshToken)) {
            throw new JWTVerificationException("유효하지 않은 refresh token");
        }

        String accessToken = JWT.create()
                .withSubject(member.getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessValidity))
                .withClaim("roles", member.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .withClaim("nickname", member.getNickname())
                .sign(Algorithm.HMAC256(secretKey));

        String newRefreshToken = JWT.create()
                .withSubject(member.getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshValidity))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(secretKey));

        updateRefreshToken(member.getMemberId(), newRefreshToken);

        Map<String, String> accessTokenResponseMap = new HashMap<>();

        accessTokenResponseMap.put(AT_HEADER, accessToken);
        accessTokenResponseMap.put(RT_HEADER, newRefreshToken);
        return accessTokenResponseMap;
    }

    @Override
    public void updateNickname(String accessToken, String newNickname) {

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);

        String memberId = decodedJWT.getSubject();

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 아이디"));

        member.updateNickname(newNickname);
    }

    @Override
    public Member findByAccessToken(String accessToken) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);

        String memberId = decodedJWT.getSubject();

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 아이디"));

        return member;
    }

    @Override
    public Member uploadImage(String accessToken, MultipartFile file) throws IOException {


        return null;
    }


}
