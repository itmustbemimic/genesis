package io.neond.genesis.service;

import io.neond.genesis.domain.role.RoleToMemberRequestDto;

public interface RoleService {
    Long saveRole(String roleName);
    Long permitMember(RoleToMemberRequestDto requestDto);
}
