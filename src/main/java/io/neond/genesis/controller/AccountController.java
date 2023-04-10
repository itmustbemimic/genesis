package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.response.ConsumptionResponseDto;
import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.dto.response.TicketSet;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import io.neond.genesis.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Tag(name = "정산페이지 api", description = "월별 총 발행량, 유저 소모량, 현재유통량....")
public class AccountController {
    private final TicketHistoryRepository ticketHistoryRepository;
    private final AccountService accountService;

    @GetMapping("/issued")
    public IssuedResponseDto issuedMonthly() {
        return accountService.issued();
    }

    @GetMapping("/issued/details")
    public void issuedDetails() {

    }

    @GetMapping("/consumption")
    public List<ConsumptionResponseDto> userConsumption() {
        return ticketHistoryRepository.consumptionMonthly();
    }

    @GetMapping("/consumption/details")
    public void consumptionDetails() {

    }

    @GetMapping("/circulation")
    public List<TicketSet> circulation() {
        return ticketHistoryRepository.circulation();
    }

    @GetMapping("/circulation/details")
    public void circulationDetails() {

    }
}
