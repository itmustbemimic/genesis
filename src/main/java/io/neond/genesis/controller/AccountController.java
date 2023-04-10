package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.response.ConsumptionResponseDto;
import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.dto.response.TicketSet;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import io.neond.genesis.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Tag(name = "정산페이지 api", description = "월별 총 발행량, 유저 소모량, 현재유통량....")
public class AccountController {
    private final TicketHistoryRepository ticketHistoryRepository;
    private final AccountService accountService;

    @Operation(summary = "처음부터 지금까지 총 발행량")
    @GetMapping("/issued")
    public IssuedResponseDto issued() {
        return accountService.issued();
    }

    @GetMapping("/issued/details/monthly")
    public List<TicketSet> issuedMonthly(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return accountService.issuedMonthly(date);
    }

    @GetMapping("/issued/details/daily")
    public List<TicketSet> issuedDaily(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return accountService.issuedDaily(date);
    }

    @GetMapping("/issued/details/custom")
    public void issuedDetails(@RequestParam Date startDate, @RequestParam Date endDate) {

    }

    @Operation(summary = "월별 유저소모량. 최근 5개월만")
    @GetMapping("/consumption")
    public List<ConsumptionResponseDto> userConsumption() {
        return ticketHistoryRepository.consumptionMonthly();
    }

    @GetMapping("/consumption/details")
    public void consumptionDetails() {

    }

    @Operation(summary = "처음부터 지금까지 유통량 = 총 발행량 - 총 소모량")
    @GetMapping("/circulation")
    public List<TicketSet> circulation() {
        return ticketHistoryRepository.circulation();
    }

    @GetMapping("/circulation/details")
    public void circulationDetails() {

    }
}
