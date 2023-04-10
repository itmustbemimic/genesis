package io.neond.genesis.service;

import io.neond.genesis.domain.dto.response.IssuedResponseDto;
import io.neond.genesis.domain.dto.response.TicketSet;

import java.util.Date;
import java.util.List;

public interface AccountService {
    IssuedResponseDto issued();
    List<TicketSet> issuedMonthly(Date date);
    List<TicketSet> issuedDaily(Date date);
    List<TicketSet> issuedCustom(Date startDate, Date endDate);

}
