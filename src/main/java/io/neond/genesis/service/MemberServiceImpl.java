package io.neond.genesis.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.neond.genesis.domain.dto.response.RankingResponseDto;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.dto.request.MemberCreateDto;
import io.neond.genesis.domain.entity.MemberGameResult;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.repository.MemberGameResultRepository;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.entity.Role;
import io.neond.genesis.domain.repository.TicketRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private final MemberGameResultRepository memberGameResultRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmazonS3 amazonS3;
    private final AmazonDynamoDB amazonDynamoDB;
    private final DynamoDBMapper dbMapper;

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.secret.qr")
    private String qrSecretKey;
    @Value("${jwt.access.validity}")
    private Long accessValidity;
    @Value("${jwt.refresh.validity}")
    private Long refreshValidity;
    @Value("${jwt.qr.validity}")
    private Long qrValidity;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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
                .withClaim("uuid", member.getUuid())
                .withIssuedAt(new Date(System.currentTimeMillis()))
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
    public void updateNickname(Member member, String newNickname) {
        member.updateNickname(newNickname);
    }

    @Override
    public Member findMemberByAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String accessToken = authorizationHeader.substring(TOKEN_HEADER_PREFIX.length());

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(accessToken);

        String memberId = decodedJWT.getSubject();

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 아이디"));

        return member;
    }

    @Override
    public String uploadImage(Member member, MultipartFile file) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getInputStream().available());

        amazonS3.putObject(bucket, member.getUuid(), file.getInputStream(), objectMetadata);

        return amazonS3.getUrl(bucket, member.getMemberId()).toString();
    }

    @Override
    public ResponseEntity<byte[]> getImage(String uuid) throws IOException {

        try {

            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, uuid));
            S3ObjectInputStream objectInputStream = s3Object.getObjectContent();


            byte[] bytes = IOUtils.toByteArray(objectInputStream);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.IMAGE_PNG);
            httpHeaders.setContentLength(bytes.length);
            httpHeaders.setContentDispositionFormData("attatchment", uuid);

            return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);

        } catch (AmazonS3Exception e) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity getQrToken(Member member) {
        String qrToken = JWT.create()
                .withSubject(member.getMemberId())
                .withExpiresAt(new Date(System.currentTimeMillis() + qrValidity))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(qrSecretKey));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"qrToken\" : \"" + qrToken + "\"}";

        return new ResponseEntity(body, headers, HttpStatus.OK);
    }

    @Override
    public List<MemberGameResult> getMemberGameResult(Member member) {
        return null;
    }

    @Override
    public List<RankingResponseDto> getWeeklyRank(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);

        cal.add(Calendar.DATE, 2 - cal.get(Calendar.DAY_OF_WEEK));
        String weekStart = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 9 - cal.get(Calendar.DAY_OF_WEEK));
        String weekEnd = dateFormat.format(cal.getTime());

        return memberGameResultRepository.getRank(weekStart, weekEnd);
    }

    @Override
    public List<RankingResponseDto> getMonthlyRank(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DAY_OF_MONTH));
        String monthStart = dateFormat.format(cal.getTime());
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH) +1);
        String monthEnd = dateFormat.format(cal.getTime());

        return memberGameResultRepository.getRank(monthStart, monthEnd);
    }
}
