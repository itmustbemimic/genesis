package io.neond.genesis.controller;

import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Tag(name = "정산페이지 api", description = "월별 총 발행량, 유저 소모량, 현재유통량....")
public class AccountController {
    private final TicketHistoryRepository ticketHistoryRepository;

    @GetMapping("/issued")
    public List<IssuedResponseDto> issuedMonthly() {
        return ticketHistoryRepository.issuedMonthly();

    }

    @GetMapping("/issued/details")
    public void issuedDetails() {

    }

    @GetMapping("/consumption")
    public void userConsumption() {

    }

    @GetMapping("/consumption/details")
    public void consumptionDetails() {

    }

    @GetMapping("/circulation")
    public void circulation() {

    }

    @GetMapping("/circulation/details")
    public void circulationDetails() {

    }
}
