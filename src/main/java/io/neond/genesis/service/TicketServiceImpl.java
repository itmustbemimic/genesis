package io.neond.genesis.service;

import io.neond.genesis.domain.dto.addTicketRequestDto;
import io.neond.genesis.domain.entity.Member;
import io.neond.genesis.domain.entity.Role;
import io.neond.genesis.domain.entity.Ticket;
import io.neond.genesis.domain.repository.MemberRepository;
import io.neond.genesis.domain.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService{

    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;

    @Override
    public String addTickets(addTicketRequestDto requestDto) {
        Member member = memberRepository.findByMemberId(requestDto.getMemberId()).orElseThrow(() -> new RuntimeException("찾을 수 없는 아이디"));

        Ticket ticket = member.getTicket();

        Ticket update = new Ticket(
                ticket.getId(),
                ticket.getBlack() + requestDto.getBlackAmount(),
                ticket.getRed() + requestDto.getRedAmount(),
                ticket.getGold() + requestDto.getGoldAmount()
                );

        ticketRepository.save(update);

        return "sss";
    }
}
