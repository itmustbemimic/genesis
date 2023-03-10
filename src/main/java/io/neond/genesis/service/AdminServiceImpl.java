package io.neond.genesis.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.neond.genesis.domain.dto.ErrorResponse;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.neond.genesis.security.Constants.TOKEN_HEADER_PREFIX;

@RequiredArgsConstructor
@Service
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final MemberRepository memberRepository;
    @Value("${jwt.secret.qr")
    private String qrSecretKey;

    @Override
    public ResponseEntity verifyQrToken(String qrToken) {
        log.info("hi");
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(qrSecretKey)).build();

        try {
            log.info(qrToken.substring(TOKEN_HEADER_PREFIX.length()));
            DecodedJWT decodedJWT = verifier.verify(qrToken.substring(TOKEN_HEADER_PREFIX.length()));
            Optional<Member> member = memberRepository.findByMemberId(decodedJWT.getSubject());

            return new ResponseEntity(member.get().getName(), HttpStatus.OK);

        } catch (TokenExpiredException e) {
            log.error(String.valueOf(e));
            ErrorResponse errorResponse = new ErrorResponse(400, "만료된 qr 코드");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);

        } catch (JWTVerificationException e) {
            log.error(String.valueOf(e));
            ErrorResponse errorResponse = new ErrorResponse(400, "유효하지 않은 qr 코드");
            return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }



}
