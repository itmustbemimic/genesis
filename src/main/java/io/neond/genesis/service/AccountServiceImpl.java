package io.neond.genesis.service;

import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.dto.response.TicketHistoryResponseDto;
import io.neond.genesis.domain.dto.response.TicketSet;
import io.neond.genesis.domain.repository.TicketHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final TicketHistoryRepository ticketHistoryRepository;
    private final MemberService memberService;
    @Override
    public IssuedResponseDto issued() {
        return IssuedResponseDto.builder()
               .userBuy(ticketHistoryRepository.issuedUserBuy())
               .prize(ticketHistoryRepository.issuedPrize())
               .build();
    }

    @Override
    public List<TicketSet> issuedMonthly(Date date) {
        List<String> border = memberService.getMonthBorder(date);
        return ticketHistoryRepository.issuedBetween(border.get(0), border.get(1));
    }

    @Override
    public List<TicketSet> issuedDaily(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day = dateFormat.format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        String day2 = dateFormat.format(cal.getTime());
        return ticketHistoryRepository.issuedBetween(day, day2);
    }

    @Override
    public List<TicketHistoryResponseDto> issuedDailyList(Date date) {
        return ticketHistoryRepository.findByAmountGreaterThanAndDateContainsAndFlagNotContains(0, formatDate(date), "gift");
    }

    @Override
    public List<TicketSet> issuedCustom(Date startDate, Date endDate) {
        return ticketHistoryRepository.issuedBetween(formatDate(startDate), formatDate(endDate));
    }

    @Override
    public List<TicketHistoryResponseDto> issuedCustomList(Date startDate, Date endDate) {
        return ticketHistoryRepository.findByDateBetweenAndFlagNotContainsOrderByDateAsc(formatDate(startDate), formatDate(endDate), "gift");
    }

    @Override
    public List<TicketSet> consumptionDetails(Date startDate, Date endDate) {
        // endDate가 없으면 해당 월 출력
        if (endDate == null) {
            List<String> border = memberService.getMonthBorder(startDate);
            return ticketHistoryRepository.consumptionDetails(border.get(0), border.get(1));
        } else {
            return ticketHistoryRepository.consumptionDetails(formatDate(startDate), formatDate(endDate));
        }

    }

    @Override
    public List<TicketHistoryResponseDto> consumptionDetailsList(Date startDate, Date endDate) {
        // endDate가 없으면 해당 월 출력
        if (endDate == null) {
            List<String> border = memberService.getMonthBorder(startDate);
            return ticketHistoryRepository.findByAmountLessThanAndDateBetweenAndFlagNotContainsOrderByDateAsc(0, border.get(0), border.get(1), "gift");
        } else {
            return ticketHistoryRepository.findByAmountLessThanAndDateBetweenAndFlagNotContainsOrderByDateAsc(0, formatDate(startDate), formatDate(endDate), "gift");
        }

    }

    public String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return dateFormat.format(cal.getTime());
    }
}
