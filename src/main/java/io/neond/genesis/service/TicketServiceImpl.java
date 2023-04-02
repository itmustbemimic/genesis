package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.AddMultipleTicketRequestDto;
import io.neond.genesis.domain.dto.request.SingleTicketRequestDto;
import io.neond.genesis.domain.dto.response.MyTicketResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
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
import org.springframework.stereotype.Service;

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
    public MyTicketResponseDto addMultipleTickets(AddMultipleTicketRequestDto requestDto) {
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
    public MyTicketResponseDto addSingleTickets(SingleTicketRequestDto requestDto) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디 "));
        Ticket ticket = member.getTicket();

        Ticket update = ticket;

        switch (requestDto.getType()) {
            case "red":
                update = new Ticket(
                        ticket.getId(),
                        ticket.getBlack(),
                        ticket.getRed() + requestDto.getAmount(),
                        ticket.getGold()
                );
                break;

            case "black":
                update = new Ticket(
                        ticket.getId(),
                        ticket.getBlack() + requestDto.getAmount(),
                        ticket.getRed(),
                        ticket.getGold()
                );
                break;

            case "gold":
                update = new Ticket(
                        ticket.getId(),
                        ticket.getBlack(),
                        ticket.getRed(),
                        ticket.getGold() + requestDto.getAmount()
                );
                break;

            default:
                break;
        }

        ticketHistoryRepository.save(requestDto.toEntity());
        return ticketRepository.save(update).toResponseDto();
    }

    @Override
    public ResponseEntity useTickets(SingleTicketRequestDto requestDto) {
        Member member = memberRepository.findByUuid(requestDto.getUuid()).orElseThrow();
        Ticket ticket = member.getTicket();

        if (requestDto.getAmount() <= 0) {
            log.warn("TicketService: requestDto.getAmount 0 이하 " + member.getMemberId() + member.getUuid());
            return new ResponseEntity("비정상적인 접근", null, HttpStatus.BAD_REQUEST);
        }

        switch (requestDto.getType()) {
            case "red":
                if (ticket.getRed() - requestDto.getAmount() < 0) {
                    return new ResponseEntity("티켓 부족", null, HttpStatus.CONFLICT);
                }
                addMultipleTickets(new AddMultipleTicketRequestDto(
                        member.getUuid(),
                        0,
                        0 - requestDto.getAmount(),
                        0,
                        requestDto.getUsage()
                ));
                break;

            case "black":
                if (ticket.getBlack() - requestDto.getAmount() < 0) {
                    return new ResponseEntity("티켓 부족", null, HttpStatus.CONFLICT);
                }
                addMultipleTickets(new AddMultipleTicketRequestDto(
                        member.getUuid(),
                        0 - requestDto.getAmount(),
                        0 ,
                        0,
                        requestDto.getUsage()
                ));
                break;

            case "gold":
                if (ticket.getGold() - requestDto.getAmount() < 0) {
                    return new ResponseEntity("티켓 부족", null, HttpStatus.CONFLICT);
                }
                addMultipleTickets(new AddMultipleTicketRequestDto(
                        member.getUuid(),
                        0,
                        0,
                        0 - requestDto.getAmount(),
                        requestDto.getUsage()
                ));
                break;

            default:
                return new ResponseEntity("티켓 타입 확인", null, HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(201));
    }

    @Override
    public List<TicketHistoryResponseDto> getMyTicketHistory(Member member) {
        return ticketHistoryRepository.findByUuidOrderByDateDesc(member.getUuid());
    }

    @Override
    public List<TicketHistoryResponseDto> getMyUseTicketHistory(Member member) {
        return ticketHistoryRepository.findByUuidAndAmountLessThanOrderByDateDesc(member.getUuid(), 0);
    }

    @Override
    public List<TicketHistoryResponseDto> getMyAddTicketHistory(Member member) {
        return ticketHistoryRepository.findByUuidAndAmountGreaterThanOrderByDateDesc(member.getUuid(), 0);
    }
}
