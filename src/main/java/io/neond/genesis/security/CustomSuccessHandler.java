package io.neond.genesis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.neond.genesis.security.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access.validity}")
    private Long accessValidity;
    @Value("${jwt.refresh.validity}")
    private Long refreshValidity;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        Optional<Member> member = memberRepository.findByMemberId(user.getUsername());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + accessValidity))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .withClaim("nickname", member.get().getNickname())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(secretKey));

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshValidity))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(secretKey));

        memberService.updateRefreshToken(user.getUsername(), refreshToken);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setHeader(AT_HEADER, accessToken);
        response.setHeader(RT_HEADER, refreshToken);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put(AT_HEADER, accessToken);
        responseMap.put(RT_HEADER, refreshToken);
        new ObjectMapper().writeValue(response.getWriter(), responseMap);
    }
}
