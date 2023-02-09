package io.neond.genesis.service;

import io.neond.genesis.domain.member.MemberCreateDto;

public interface MemberService {
    Long createMember(MemberCreateDto createDto);
}
