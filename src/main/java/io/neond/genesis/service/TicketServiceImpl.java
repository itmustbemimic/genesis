package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.AddTicketRequestDto;
import io.neond.genesis.domain.dto.request.UseTicketRequestDto;
import io.neond.genesis.domain.dto.response.ErrorResponse;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.entity.TicketHistory;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import io.neond.genesis.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService{

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final TicketHistoryRepository ticketHistoryRepository;

    @Override
    public Ticket addTickets(AddTicketRequestDto requestDto) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디"));

        Ticket ticket = member.getTicket();

        Ticket update = new Ticket(
                ticket.getId(),
                ticket.getBlack() + requestDto.getBlackAmount(),
                ticket.getRed() + requestDto.getRedAmount(),
                ticket.getGold() + requestDto.getGoldAmount()
                );

        return ticketRepository.save(update);
    }

    @Override
    public ResponseEntity useTickets(UseTicketRequestDto requestDto) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow();
        ticketHistoryRepository.save(requestDto.toEntity(member.getUuid()));
        Ticket ticket = member.getTicket();

        switch (requestDto.getType()) {
            case "red":
                if (ticket.getRed() <= 0) {
                    return new ResponseEntity("티켓 부족", null, HttpStatus.CONFLICT);
                }
                addTickets(new AddTicketRequestDto(
                        member.getUuid(),
                        0,
                        0 - requestDto.getAmount(),
                        0
                ));
                break;

            case "black":
                if (ticket.getBlack() <= 0) {
                    return new ResponseEntity("티켓 부족", null, HttpStatus.CONFLICT);
                }
                addTickets(new AddTicketRequestDto(
                        member.getUuid(),
                        0 - requestDto.getAmount(),
                        0 ,
                        0
                ));
                break;

            case "gold":
                if (ticket.getGold() <= 0) {
                    return new ResponseEntity("티켓 부족", null, HttpStatus.CONFLICT);
                }
                addTickets(new AddTicketRequestDto(
                        member.getUuid(),
                        0,
                        0,
                        0 - requestDto.getAmount()
                ));
                break;

            default:
                log.info("뭘 봐 팍씨");

        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
}
