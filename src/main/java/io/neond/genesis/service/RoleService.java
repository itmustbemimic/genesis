package io.neond.genesis.service;

import io.neond.genesis.domain.dto.RoleToMemberRequestDto;

public interface RoleService {
    Long saveRole(String roleName);
    String permitMember(RoleToMemberRequestDto requestDto);
}
