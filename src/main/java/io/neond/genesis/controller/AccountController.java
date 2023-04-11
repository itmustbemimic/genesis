package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.response.ConsumptionResponseDto;
import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.dto.response.TicketSet;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import io.neond.genesis.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

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

    @Operation(summary = "한달동안 발행된 티켓")
    @GetMapping("/issued/details/monthly")
    public List<TicketSet> issuedMonthly(
            @Parameter(name = "date", description = "해당 날짜가 속한 월의 발행량. 2023-03-08 입력 시 3월 발행량 출력")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date) {
        return accountService.issuedMonthly(date);
    }

    @Operation(summary = "하루동안 발행된 티켓")
    @GetMapping("/issued/details/daily")
    public List<TicketSet> issuedDaily(
            @Parameter(name = "date", example = "2023-03-08")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date) {
        return accountService.issuedDaily(date);
    }

    @Operation(summary = "하루동안 발행된 티켓 리스트")
    @GetMapping("/issued/details/daily/list")
    public List<TicketHistoryResponseDto> issuedDailyList(
            @Parameter(name = "date", example = "2023-03-08")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date) {
        return accountService.issuedDailyList(date);
    }

    @Operation(summary = "지정된 기간동안 발행된 티켓")
    @GetMapping("/issued/details/custom")
    public List<TicketSet> issuedDetails(
            @Parameter(name = "날짜", description = "시작 날짜", example = "2023-03-01")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date startDate,

            @Parameter(name = "date", description = "마지막 날짜", example = "2023-04-27")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date endDate) {
        return accountService.issuedCustom(startDate, endDate);
    }

    @Operation(summary = "월별 유저소모량. 최근 5개월만")
    @GetMapping("/consumption")
    public List<ConsumptionResponseDto> userConsumption() {
        return ticketHistoryRepository.consumptionMonthly();
    }

    @Operation(summary = "티켓별 유저 월 소모량")
    @GetMapping("/consumption/details")
    public List<TicketSet> consumptionDetails(
            @Parameter(name = "date", description = "해당 날짜가 속한 월의 소모량. 2023-03-08 입력 시 3월 소모량 출력")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date) {
        return accountService.consumptionDetails(date);
    }

    @Operation(summary = "월별 유저 소모량 세부 내역")
    @GetMapping("/consumption/details/list")
    public List<TicketHistoryResponseDto> consumptionDetailsList(
            @Parameter(name = "date", description = "해당 날짜가 속한 월의 소모량. 2023-03-08 입력 시 3월 소모내역 출력")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date) {
        return accountService.consumptionDetailsList(date);
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
