package io.neond.genesis.service;

import io.neond.genesis.domain.dto.request.RoleToMemberRequestDto;
import org.springframework.http.ResponseEntity;

public interface RoleService {
    Long saveRole(String roleName);
    String permitMember(RoleToMemberRequestDto requestDto);
    ResponseEntity rejectMember(RoleToMemberRequestDto requestDto);
}
