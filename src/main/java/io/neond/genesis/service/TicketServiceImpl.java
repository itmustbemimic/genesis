package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.MultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto2;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import io.neond.genesis.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService{

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository ticketHistoryRepository;

    @Override
    public MyTicketResponseDto addMultipleTickets(MultipleTicketRequestDto requestDto) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디"));

        Ticket ticket = member.getTicket();

        Ticket update = new Ticket(
                ticket.getId(),
                ticket.getBlack() + requestDto.getBlackAmount(),
                ticket.getRed() + requestDto.getRedAmount(),
                ticket.getGold() + requestDto.getGoldAmount()
                );

        if (requestDto.getBlackAmount() != 0) {
            ticketHistoryRepository.save(requestDto.toEntity(member.getUuid(), "black", requestDto.getBlackAmount()));
        }

        if (requestDto.getRedAmount() != 0) {
            ticketHistoryRepository.save(requestDto.toEntity(member.getUuid(), "red", requestDto.getRedAmount()));
        }

        if (requestDto.getGoldAmount() != 0) {
            ticketHistoryRepository.save(requestDto.toEntity(member.getUuid(), "gold", requestDto.getGoldAmount()));
        }

        return ticketRepository.save(update).toResponseDto();
    }

    @Override
    public ResponseEntity<String> useMultipleTickets(MultipleTicketRequestDto requestDto) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow();
        Ticket ticket = member.getTicket();

        if (requestDto.getRedAmount() < 0 || requestDto.getBlackAmount() < 0 || requestDto.getGoldAmount() < 0) {
            log.warn("TicketService.useMultipleTickets: requestDto amount 0 미만 " + member.getMemberId() + member.getUuid());
            return new ResponseEntity<>("비정상적인 접근", null, HttpStatus.BAD_REQUEST);
        }

        if (ticket.getRed() - requestDto.getRedAmount() < 0) {
            // not enough red tickets
            return new ResponseEntity<>("레드 티켓 부족", null, HttpStatus.CONFLICT);
        }

        if (ticket.getBlack() - requestDto.getBlackAmount() < 0) {
            // not enough black tickets
            return new ResponseEntity<>("블랙 티켓 부족", null, HttpStatus.CONFLICT);
        }

        if (ticket.getGold() - requestDto.getGoldAmount() < 0) {
            // not enough gold tickets
            return new ResponseEntity<>("골드 티켓 부족", null, HttpStatus.CONFLICT);
        }

        addMultipleTickets(requestDto.useToAdd());

        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @Override
    public MyTicketResponseDto addSingleTickets(SingleTicketRequestDto requestDto, String flag) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디 "));
        Ticket ticket = member.getTicket();

        Ticket update = ticket;

        switch (requestDto.getType()) {
            case "red" -> update = new Ticket(
                    ticket.getId(),
                    ticket.getBlack(),
                    ticket.getRed() + requestDto.getAmount(),
                    ticket.getGold()
            );
            case "black" -> update = new Ticket(
                    ticket.getId(),
                    ticket.getBlack() + requestDto.getAmount(),
                    ticket.getRed(),
                    ticket.getGold()
            );
            case "gold" -> update = new Ticket(
                    ticket.getId(),
                    ticket.getBlack(),
                    ticket.getRed(),
                    ticket.getGold() + requestDto.getAmount()
            );
            default -> {
            }
        }

        ticketHistoryRepository.save(requestDto.toEntity(flag));
        return ticketRepository.save(update).toResponseDto();
    }

    @Override
    public ResponseEntity<String> useSingleTickets(SingleTicketRequestDto requestDto, String flag) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow();
        Ticket ticket = member.getTicket();

        if (requestDto.getAmount() < 0) {
            log.warn("TicketService: requestDto.getAmount 0 미만 " + member.getMemberId() + member.getUuid());
            return new ResponseEntity<>("비정상적인 접근", null, HttpStatus.BAD_REQUEST);
        }

        switch (requestDto.getType()) {
            case "red" -> {
                if (ticket.getRed() - requestDto.getAmount() < 0) {
                    return new ResponseEntity<>("티켓 부족", null, HttpStatus.CONFLICT);
                }
            }
            case "black" -> {
                if (ticket.getBlack() - requestDto.getAmount() < 0) {
                    return new ResponseEntity<>("티켓 부족", null, HttpStatus.CONFLICT);
                }
            }
            case "gold" -> {
                if (ticket.getGold() - requestDto.getAmount() < 0) {
                    return new ResponseEntity<>("티켓 부족", null, HttpStatus.CONFLICT);
                }
            }
            default -> {
                return new ResponseEntity<>("티켓 타입 확인", null, HttpStatus.BAD_REQUEST);
            }
        }

        addSingleTickets(requestDto.useToAdd(), flag);
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @Override
    public List<TicketHistoryResponseDto2> getMyTicketHistory(Member member) {
        //List<TicketHistoryResponseDto> response = ticketHistoryRepository.findByUuidOrderByDateDesc(member.getUuid());

        List<TicketHistoryResponseDto2> response = ticketHistoryRepository.findByUuidOrderByDateDesc(member.getUuid());
        List<TicketHistoryResponseDto2> res = new ArrayList<>();
        for (TicketHistoryResponseDto2 TH : response) {
            res.add(TH.renderNickname(memberRepository.findByUuid(TH.getSummary())
                    .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 유저"))
                    .getNickname()));
        }

        return res;
    }

    @Override
    public List<TicketHistoryResponseDto2> getMyTicketHistoryByDate(Member member, String s_date, String e_date) {
        return null;
    }

    @Override
    public List<TicketHistoryResponseDto2> getMyTicketHistoryByType(Member member, String type) {

        return null;
    }

    @Override
    public List<TicketHistoryResponseDto2> getMyTicketHistoryByDateAndType(Member member, String s_date, String e_date, String type) {
        if (s_date == null) s_date= "2020-01-01";
        if (e_date == null) e_date = "9999-12-31";

        List<TicketHistoryResponseDto2> response = new ArrayList<>();

        if (type == null) {
            log.info("1");
            log.info(s_date);
            log.info(e_date);
            response =
                    ticketHistoryRepository.findByUuidAndDateBetweenOrderByDateAsc(
                            member.getUuid(),
                            s_date,
                            e_date
                    );


        } else {
            log.info("2");
            response =
                    ticketHistoryRepository.findByUuidAndDateBetweenAndTypeOrderByDateAsc(
                            member.getUuid(),
                            s_date,
                            e_date,
                            type
                    );
        }


        List<TicketHistoryResponseDto2> res = new ArrayList<>();
        for (TicketHistoryResponseDto2 TH : response) {
            res.add(TH.renderNickname(memberRepository.findByUuid(TH.getSummary())
                    .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 유저"))
                    .getNickname()));
        }

        return res;
    }

    @Override
    public List<TicketHistoryResponseDto2> getMyUseTicketHistory(Member member) {
        //return ticketHistoryRepository.findByUuidAndFlagOrderByDateDesc(member.getUuid(), "game");
        List<TicketHistoryResponseDto2> response = ticketHistoryRepository.findByUuidAndAmountLessThan(member.getUuid(), 0);

        List<TicketHistoryResponseDto2> res = new ArrayList<>();
        for (TicketHistoryResponseDto2 TH : response) {
            res.add(TH.renderNickname(memberRepository.findByUuid(TH.getSummary())
                    .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 유저"))
                    .getNickname()));
        }

        return res;
        //TODO flag 바꾸기

    }

    @Override
    public List<TicketHistoryResponseDto2> getMyAddTicketHistory(Member member) {
        //return ticketHistoryRepository.findByUuidAndFlagOrderByDateDesc(member.getUuid(), "charge");
        List<TicketHistoryResponseDto2> response = ticketHistoryRepository.findByUuidAndAmountGreaterThan(member.getUuid(), 0);

        List<TicketHistoryResponseDto2> res = new ArrayList<>();
        for (TicketHistoryResponseDto2 TH : response) {
            res.add(TH.renderNickname(memberRepository.findByUuid(TH.getSummary())
                    .orElseThrow(() -> new UsernameNotFoundException("찾을 수 없는 유저"))
                    .getNickname()));
        }

        return res;
        //TODO flag 바꾸기

    }
}
